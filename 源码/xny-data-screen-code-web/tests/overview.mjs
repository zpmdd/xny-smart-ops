import assert from 'node:assert/strict'
import { readSnapshot } from '../src/data/overview.mjs'
const at = time => readSnapshot(Date.parse(time + '+08:00'))
const start = at('2026-11-19T10:00:00')
assert.deepEqual(start.overview.totalItems.childs.map(p => p.value), [1, 2, 1, 1, 0])
assert.equal(start.overview.totalItems.value, 5)
assert.equal(start.overview.grossCapacity.value, 200.41)
assert.equal(start.station.otherData.childs[0].value, 100)
assert.deepEqual(start, at('2026-11-19T10:00:00'))
let previous = start, up = false, down = false
for (let i = 1; i <= 120; i++) {
  const snap = readSnapshot(Date.parse('2026-11-19T10:00:00+08:00') + i * 5000)
  const { overview, station } = snap
  assert.ok(station.power <= 100 && station.power >= 0)
  up ||= station.power > previous.station.power
  down ||= station.power < previous.station.power
  assert.ok(overview.grossGeneration.value >= previous.overview.grossGeneration.value)
  assert.ok(overview.totalRevenue.value >= previous.overview.totalRevenue.value)
  for (const group of [overview.grossCapacity, overview.grossGeneration, overview.totalRevenue]) {
    assert.equal(group.value, Number(group.childs.reduce((a, b) => a + b.value, 0).toFixed(2)))
    assert.equal(group.childs[2].value, 0)
  }
  assert.equal(station.powerChart.childs[0].childs.at(-1).value, station.power)
  assert.equal(station.energyChart.childs[0].childs.at(-1).value, station.fdData.childs[1].value)
  previous = snap
}
assert.ok(up && down)
assert.ok(previous.station.fdData.childs[0].value > start.station.fdData.childs[0].value)
for (const [end, next, monthReset, yearReset] of [
  ['2026-11-19', '2026-11-20', false, false],
  ['2026-11-30', '2026-12-01', true, false],
  ['2026-12-31', '2027-01-01', true, true],
]) {
  const old = at(end + 'T23:59:59').station
  const current = at(next + 'T00:00:00').station
  assert.equal(current.power, 0)
  assert.equal(current.fdData.childs[0].value, old.fdData.childs[0].value)
  assert.equal(current.fdData.childs[1].value, 0)
  assert.equal(current.fdData.childs[2].value, monthReset ? 0 : old.fdData.childs[2].value)
  assert.equal(current.fdData.childs[3].value, yearReset ? 0 : old.fdData.childs[3].value)
  assert.equal(current.energyChart.childs[0].childs.at(-2).value, old.fdData.childs[1].value)
}

const metrics = {
  generation: 8000.02, revenue: 2800.01, month: 400.05, monthRevenue: 140.01,
  year: 7600.03, yearRevenue: 2660.01, safetyDays: 215, annualHours: 802.1,
  co2Reduction: 6.12, coalSavings: 2.35,
}
process.env.VUE_APP_WEIJIAZHUANG_TOTALS = JSON.stringify(metrics)
const { readSnapshot: readConfigured } = await import('../src/data/overview.mjs?configured')
for (const time of ['2026-11-19T12:30:00', '2026-12-01T10:00:00']) {
  const original = at(time).station
  const { station } = readConfigured(Date.parse(time + '+08:00'))
  assert.deepEqual(station.fdData.childs.map(p => p.value), [metrics.generation, original.fdData.childs[1].value, metrics.month, metrics.year])
  assert.deepEqual(station.syData.childs.map(p => p.value), [metrics.revenue, original.syData.childs[1].value, metrics.monthRevenue, metrics.yearRevenue])
  assert.deepEqual(station.devOpsData.childs.slice(0, 2).map(p => p.value), [metrics.safetyDays, '802.10'])
  assert.deepEqual(station.socialBenefit.childs.map(p => p.value), [metrics.co2Reduction, metrics.coalSavings])
  assert.deepEqual(station.energyChart, original.energyChart)
}
const configured = readConfigured(Date.parse('2026-11-19T00:00:00+08:00')).overview
assert.equal(configured.grossGeneration.childs[1].value, 46004.02)
assert.equal(configured.totalRevenue.value, 16382.01)
assert.deepEqual(configured.socialBenefit.childs.map(p => p.value), [44.16, 16.08])
