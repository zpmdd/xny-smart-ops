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

export function cameraPreview({ getInfo, nextTick, createPlayer, show, status }) {
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
      session.player = createPlayer({
        id: session.id, url, accessToken: token, template: 'pcLive', maxReloadTime: 0,
        handleSuccess() {
          if (current !== session) return
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
