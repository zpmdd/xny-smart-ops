// This SDK starts playback in its constructor and stores resources outside the Vue component.
let releasing = Promise.resolve(true)
const logKey = 'xny.camera.preview.v1'
let entries
function readLog() {
  if (entries) return entries
  try {
    const saved = JSON.parse(sessionStorage.getItem(logKey))
    if (Array.isArray(saved)) return (entries = saved.slice(-120))
  } catch {}
  return (entries = [])
}
export function cameraLogText() {
  return JSON.stringify({ format: 'xny-camera-1', events: readLog() }, null, 2)
}
function record(id, event, code) {
  const line = { time: new Date().toISOString(), session: id, event }
  if (code !== undefined) line.code = code
  const log = readLog()
  log.push(line)
  if (log.length > 120) log.shift()
  try { sessionStorage.setItem(logKey, JSON.stringify(log)) } catch {}
}
function numericCode(value) {
  const code = value && (value.retcode ?? value.errorCode ?? value.code ?? value.response?.status)
  return /^-?\d{1,8}$/.test(String(code)) ? Number(code) : undefined
}
async function releaseStep(player, method, report) {
  let timer
  try {
    await Promise.race([player[method](), new Promise((_, reject) => {
      timer = setTimeout(() => reject({ code: 'timeout' }), 3500)
    })])
    report(method + ' 完成')
    return true
  } catch (error) {
    report(method + (error?.code === 'timeout' ? ' 超时' : ' 失败'), numericCode(error))
    return false
  } finally {
    clearTimeout(timer)
  }
}

function observeSdkMessage(id, notify) {
  // EZUIKit 8.0.8 writes some autoplay failures to its overlay without calling handleError.
  const host = document.getElementById(id).parentElement
  const observer = new MutationObserver(() => {
    const hint = document.getElementById(id + '-loading-item-txt')
    if (hint && host.contains(hint)) notify(hint.textContent, hint)
  })
  observer.observe(host, { childList: true, characterData: true, subtree: true })
  return () => observer.disconnect()
}

async function deviceInfo(url, token, signal) {
  const stream = new URL(url)
  const serial = stream.pathname.split('/')[1]
  if (stream.protocol !== 'ezopen:' || stream.hostname !== 'open.ys7.com' || !/^[A-Za-z0-9]{7,16}$/.test(serial)) return
  const response = await fetch('https://open.ys7.com/api/lapp/device/info', {
    method: 'POST', body: new URLSearchParams({ deviceSerial: serial, accessToken: token }),
    signal, credentials: 'omit', redirect: 'error', referrerPolicy: 'no-referrer',
  })
  const result = await response.json()
  const code = numericCode(result)
  if (code === 10026) return { message: '萤石账号设备数量超出个人版限制', code }
  if (code !== 200) return { message: '萤石设备状态查询失败', code }
  if (result.data?.status === 0) return { message: '萤石设备离线，请检查供电和网络' }
  if (result.data?.status === 1) return { message: '萤石设备在线，预览连接失败' }
}

export function cameraPreview({ getInfo, nextTick, createPlayer, show, status, observeMessage = observeSdkMessage }) {
  let current = null
  let closing = null
  async function open() {
    if (current || closing) return
    const session = { id: 'cam-' + Date.now().toString(36) + '-' + Math.random().toString(36).slice(2, 7) }
    current = session
    show(session.id)
    status('正在获取播放信息')
    const report = (event, code) => record(session.id, event, code)
    report('打开监控')
    try {
      if (!(await releasing)) {
        if (current === session) status('上次播放未能释放，请关闭此标签页后重新进入')
        report('上次播放未能释放')
        return
      }
      if (current !== session) return
      report('请求播放信息')
      const response = await getInfo()
      if (current !== session) return
      const { url, token } = response?.data || {}
      if (typeof url !== 'string' || !url || typeof token !== 'string' || !token) {
        report('播放信息不完整', numericCode(response))
        status('未获取到完整播放信息')
        return
      }
      report('收到播放信息')
      await nextTick()
      if (current !== session) return
      status('正在连接摄像头')
      report('初始化播放器')
      session.disconnect = observeMessage(session.id, (text, hint) => {
        if (current !== session) return
        let event, message
        let missingDeviceStatus = false
        if (text.startsWith('设备不在线')) {
          const deviceStatus = session.player?.deviceInfo?.status
          if (deviceStatus === 0) {
            event = '萤石设备离线（status=0）'
            message = '设备不在线，请检查供电和网络'
          } else {
            missingDeviceStatus = deviceStatus !== 1
            event = deviceStatus === 1 ? 'SDK 提示离线，萤石设备状态为在线' : 'SDK 提示离线，萤石设备状态未确认'
            message = event
          }
        } else if (text.startsWith('同时观看人数达到账号最大限制')) {
          event = message = '同时观看人数达到账号最大限制'
        } else {
          return
        }
        if (session.lastHint === event) return
        session.lastHint = event
        report(event)
        status(message)
        // This SDK also renders “offline” when the device-info API rejects the account.
        if (missingDeviceStatus && !session.lookup) {
          session.lookup = new AbortController()
          const timer = setTimeout(() => session.lookup.abort(), 5000)
          deviceInfo(url, token, session.lookup.signal).then(result => {
            if (current !== session || session.lastHint !== event || !result) return
            report(result.message, result.code)
            const label = result.message + (result.code === undefined ? '' : '（' + result.code + '）')
            status(label)
            if (result.code === 10026 && hint?.isConnected && hint.textContent === text) hint.textContent = label
          }).catch(() => {
            if (current === session && session.lastHint === event) report('未取得萤石设备信息')
          }).finally(() => clearTimeout(timer))
        }
      })
      session.player = createPlayer({
        id: session.id, url, accessToken: token, template: 'pcLive', maxReloadTime: 0,
        handleSuccess() {
          if (current !== session) return
          session.lastHint = ''
          report('SDK 播放成功')
          status('')
        },
        handleError(error) {
          if (current !== session) return
          const code = numericCode(error)
          report('SDK 播放报错', code)
          status(code === undefined ? '播放异常，请导出监控日志' : '播放异常，错误码 ' + code)
        },
      })
    } catch (error) {
      if (current === session) {
        report('打开监控失败', numericCode(error))
        status('监控打开失败，请导出监控日志')
      }
    }
  }
  function close() {
    if (!current) return closing || Promise.resolve()
    const session = current
    current = null // Ignore late API responses and callbacks from this particular instance.
    session.disconnect?.()
    session.lookup?.abort()
    const report = (event, code) => record(session.id, event, code)
    report('关闭监控')
    if (!session.player) {
      show('')
      return Promise.resolve()
    }
    status('正在停止播放')
    releasing = (async () => {
      await releaseStep(session.player, 'stop', report)
      return releaseStep(session.player, 'destroy', report)
    })()
    closing = releasing.finally(() => {
      closing = null
      show('')
    })
    return closing
  }
  return { open, close }
}
