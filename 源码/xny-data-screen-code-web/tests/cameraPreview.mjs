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
