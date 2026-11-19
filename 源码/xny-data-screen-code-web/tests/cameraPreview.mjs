import assert from 'node:assert/strict'
import { cameraPreview, cameraLogText } from '../src/utils/cameraPreview.mjs'

const pending = () => {
  let resolve
  const promise = new Promise(done => { resolve = done })
  return { promise, resolve }
}
const ready = { data: { url: 'fixture-stream-private', token: 'fixture-token-private' } }
function fixture(overrides = {}) {
  const calls = []
  let callbacks
  const preview = cameraPreview({
    getInfo: async () => { calls.push('info'); return ready },
    nextTick: async () => {},
    observeMessage: () => () => {},
    createPlayer(options) {
      callbacks = options
      calls.push('create')
      return {
        stop() { calls.push('stop') },
        destroy() { calls.push('destroy') },
        play() { assert.fail('SDK already autoplays') },
      }
    },
    show: id => calls.push(id ? 'visible' : 'hidden'),
    status: msg => calls.push(msg),
    ...overrides,
  })
  return { preview, calls, get callbacks() { return callbacks } }
}

const one = fixture()
await Promise.all([one.preview.open(), one.preview.open()])
assert.equal(one.calls.filter(x => x === 'info').length, 1)
assert.equal(one.calls.filter(x => x === 'create').length, 1)
one.callbacks.handleSuccess()
one.callbacks.handleError({ retcode: 5546, msg: 'fixture-secret' })
await Promise.all([one.preview.close(), one.preview.close()])
assert.deepEqual(one.calls.slice(-3), ['stop', 'destroy', 'hidden'])
const before = cameraLogText()
one.callbacks.handleError({ retcode: 9999 })
assert.equal(cameraLogText(), before)
assert.match(before, /5546/)
assert.doesNotMatch(before, /fixture-/)

let sdkMessage
let disconnected = false
const offline = fixture({
  observeMessage(id, notify) {
    sdkMessage = notify
    return () => { disconnected = true }
  },
  createPlayer: () => ({ deviceInfo: { status: 0 }, stop() {}, destroy() {} }),
})
await offline.preview.open()
sdkMessage('设备不在线，请优化网络后重启设备再试')
sdkMessage('设备不在线，请优化网络后重启设备再试')
sdkMessage('fixture-secret')
assert.deepEqual(offline.calls.slice(-1), ['设备不在线，请检查供电和网络'])
assert.equal(JSON.parse(cameraLogText()).events.filter(e => e.event === '萤石设备离线（status=0）').length, 1)
await offline.preview.close()
assert.ok(disconnected)
const afterClose = cameraLogText()
sdkMessage('同时观看人数达到账号最大限制，请联系客服')
assert.equal(cameraLogText(), afterClose)

const unknown = fixture({ observeMessage(id, notify) { sdkMessage = notify; return () => {} } })
await unknown.preview.open()
sdkMessage('设备不在线，请优化网络后重启设备再试')
assert.match(unknown.calls.at(-1), /未确认/)
sdkMessage('同时观看人数达到账号最大限制，请联系客服')
assert.match(unknown.calls.at(-1), /观看人数/)
await unknown.preview.close()

const requests = []
let reply = pending()
globalThis.fetch = (url, options) => {
  requests.push({ url, options })
  return reply.promise
}
const ezInfo = { data: { url: 'ezopen://open.ys7.com/TEST00001/1.live', token: 'fixture-token-private' } }
const lookup = fixture({
  getInfo: async () => ezInfo,
  observeMessage(id, notify) { sdkMessage = notify; return () => {} },
})
await lookup.preview.open()
const quotaHint = { isConnected: true, textContent: '设备不在线' }
sdkMessage(quotaHint.textContent, quotaHint)
sdkMessage('设备不在线')
assert.equal(requests.length, 1)
assert.equal(requests[0].url, 'https://open.ys7.com/api/lapp/device/info')
assert.equal(requests[0].options.body.get('deviceSerial'), 'TEST00001')
assert.equal(requests[0].options.redirect, 'error')
reply.resolve({ json: async () => ({ code: '10026', msg: 'fixture-private' }) })
await new Promise(resolve => setImmediate(resolve))
assert.equal(lookup.calls.at(-1), '萤石账号设备数量超出个人版限制（10026）')
assert.equal(quotaHint.textContent, lookup.calls.at(-1))
assert.equal(JSON.parse(cameraLogText()).events.at(-1).code, 10026)
await lookup.preview.close()
reply = pending()
await lookup.preview.open()
sdkMessage('设备不在线')
await lookup.preview.close()
assert.ok(requests[1].options.signal.aborted)
const closedLog = cameraLogText()
reply.resolve({ json: async () => ({ code: 10026 }) })
await new Promise(resolve => setImmediate(resolve))
assert.equal(cameraLogText(), closedLog)

const info = pending()
const late = fixture({ getInfo: () => info.promise })
const opening = late.preview.open()
await Promise.resolve()
await late.preview.close()
info.resolve(ready)
await opening
assert.ok(!late.calls.includes('create'))

const stop = pending()
const order = []
const old = fixture({ createPlayer: () => ({
  stop() { order.push('stop'); return stop.promise },
  destroy() { order.push('destroy') },
}) })
const next = fixture({ getInfo: async () => { order.push('info'); return ready } })
await old.preview.open()
const closing = old.preview.close()
const reentry = next.preview.open()
await Promise.resolve()
assert.deepEqual(order, ['stop'])
stop.resolve()
await Promise.all([closing, reentry])
assert.deepEqual(order, ['stop', 'destroy', 'info'])
await next.preview.close()

const bad = fixture({ getInfo: async () => ({ code: 1, data: {} }) })
await bad.preview.open()
assert.ok(!bad.calls.includes('create'))
await bad.preview.close()
const failed = fixture({ createPlayer: () => ({
  stop() { throw new Error('fixture-private') },
  destroy() { throw new Error('fixture-private') },
}) })
await failed.preview.open()
await failed.preview.close()
const blocked = fixture()
await blocked.preview.open()
assert.ok(!blocked.calls.includes('create'))
assert.match(cameraLogText(), /destroy 失败/)
assert.doesNotMatch(cameraLogText(), /fixture-/)
console.log('Camera open, callbacks, close, delayed requests, route re-entry and sanitized diagnostics passed.')
