const round = value => Number(value.toFixed(2))
const metric = (label, value, unit) => ({ label, value, unit })
const group = (label, childs) => ({ label, childs })
const sum = values => round(values.reduce((total, value) => total + value, 0))

export const projectTypes = ['分布式光伏', '集中式光伏', '储能项目', '充电项目', '风电项目']
export const weijiazhuangStation = {
  id: 'weijiazhuang', type: 2, projectName: '韦家庄光伏电站', city: '宝鸡市', path: '/weijiazhuang', capacity: 100,
  contact: process.env.VUE_APP_WEIJIAZHUANG_CONTACT || '',
  extension: process.env.VUE_APP_WEIJIAZHUANG_PHONE || '',
}
export const projects = [
  { id: 1, type: 1, projectName: '西安光储充一体化项目', city: '西安市', path: '/fbsgf', capacity: 0.41 },
  { id: 2, type: 2, projectName: '延安光伏电站', city: '延安市', path: '/jzsgf', capacity: 100 },
  weijiazhuangStation,
  { id: 3, type: 3, projectName: '西安光储充一体化项目', city: '西安市', path: '/cnxm' },
  { id: 4, type: 4, projectName: '西安光储充一体化项目', city: '西安市', path: '/cdxm' },
]

// 参考日零点的期初量。电量为万 kWh，容量为 MW，收益为万元。
const anchor = Date.parse('2026-11-19T00:00:00+08:00')
const dayMs = 86400000
const hourMs = 3600000
const step = 30 // seconds
const tariff = 0.3545
const opening = [
  { project: projects[0], generation: 146, revenue: 109.5, month: 1.2, year: 42, yieldFactor: 0.55, rate: 0.75 },
  { project: projects[1], generation: 38004, revenue: 13472.5, month: 574, year: 12525, yieldFactor: 0.62, rate: tariff },
  { project: projects[2], generation: 3153, revenue: 3153 * tariff, month: 517, year: 3153, yieldFactor: 0.65, rate: tariff },
]
const weather = [0.91, 0.83, 0.98, 0.87, 0.94, 0.78, 0.89]
const week = weather.reduce((a, b) => a + b, 0)
const weekIndex = day => ((day % 7) + 7) % 7
function precedingWeather(day) {
  return Math.floor(day / 7) * week + weather.slice(0, weekIndex(day)).reduce((a, b) => a + b, 0)
}

// 对同一条晴空与云量曲线积分，刷新网页和切换电脑都可按北京时间复算。
function daylight(seconds) {
  if (seconds <= 7 * 3600 || seconds >= 18 * 3600) return 0
  const sun = Math.sin(Math.PI * (seconds / 3600 - 7) / 11)
  return sun * (0.91 + 0.06 * Math.sin(seconds / 65) + 0.03 * Math.sin(seconds / 281))
}
const curve = Array.from({ length: 86400 / step + 1 }, (_, i) => daylight(i * step))
const integral = [0]
for (let i = 1; i < curve.length; i++) {
  integral.push(integral[i - 1] + (curve[i - 1] + curve[i]) * step / 7200)
}
const fullDay = integral[integral.length - 1]
function solarAt(seconds) {
  const i = Math.floor(seconds / step)
  const fraction = seconds / step - i
  const power = curve[i] + ((curve[i + 1] || 0) - curve[i]) * fraction
  return { power, hours: integral[i] + (curve[i] + power) * fraction * step / 7200 }
}
function calendar(now) {
  const elapsed = Math.max(0, now - anchor)
  const date = new Date(anchor + elapsed + 8 * hourMs)
  const year = date.getUTCFullYear()
  const startDay = month => (Date.UTC(year, month, 1) - 8 * hourMs - anchor) / dayMs
  return { day: Math.floor(elapsed / dayMs), seconds: elapsed % dayMs / 1000, monthStart: startDay(date.getUTCMonth()), yearStart: startDay(0) }
}
function reading(site, clock, sun) {
  const { day, monthStart, yearStart } = clock
  const scale = site.project.capacity * site.yieldFactor
  const today = scale * weather[weekIndex(day)] * sun.hours / 10
  const beforeDay = d => scale * fullDay * precedingWeather(d) / 10
  const delta = beforeDay(day) + today
  const period = (start, initial) => round(start < 0 ? initial + delta : delta - beforeDay(start))
  return {
    ...site.project, scale, power: round(scale * weather[weekIndex(day)] * sun.power),
    generation: round(site.generation + delta), revenue: round(site.revenue + delta * site.rate),
    day: round(today), month: period(monthStart, site.month), year: period(yearStart, site.year),
  }
}
const counts = projectTypes.map((label, index) => metric(label, projects.filter(p => p.type === index + 1).length))
function generationMetric(sites, label, unit, select) {
  const childs = [1, 2, 5].map(type => metric(projectTypes[type - 1], sum(sites.filter(p => p.type === type).map(select))))
  return { label, unit, value: sum(childs.map(item => item.value)), childs }
}
function benefits(generation) {
  return group('社会效益', [
    metric('CO₂减排', round(generation * 0.000997), '万吨'),
    metric('标煤节约', round(generation * 0.00036), '万吨'),
  ])
}
export function titleText({ label, unit, value }) {
  return label + (unit ? '（' + unit + '）' : '') + (value == null ? '' : '：' + value)
}
function makeOverview(sites, day) {
  const energy = generationMetric(sites, '总发电量', '万kWh', p => p.generation)
  const capacity = generationMetric(sites, '总装机容量', 'MW', p => p.capacity)
  return {
    totalItems: { ...group('项目总数', counts), value: projects.length },
    grossGeneration: energy, grossCapacity: capacity,
    totalRevenue: generationMetric(sites, '总收益', '万元', p => p.revenue),
    totalAccess: { ...group('发电数据统计', [
      metric('日发电量', sum(sites.map(p => p.day))), metric('月发电量', sum(sites.map(p => p.month))),
    ]), unit: '万kWh' },
    devOpsData: group('运维管理', [metric('安全生产', 1269 + day, '天'),
      metric('安全发电', round(energy.value * 10 / capacity.value), '小时'), metric('安全设备数量', 477, '台'), metric('设备完好率', '100%')]),
    runStatus: group('运行状况分析', [metric('正常运行设备数', 477, '台'), metric('异常设备数', 0, '台')]),
    socialBenefit: benefits(energy.value),
  }
}
function stationData(site, clock) {
  const { day, seconds } = clock
  const periods = ['累计', '今日', '本月', '本年']
  const totals = [site.generation, site.day, site.month, site.year]
  const powerPoints = Array.from({ length: 31 }, (_, i) => {
    const t = seconds - (30 - i) * 60
    const s = (t + 86400) % 86400
    const label = [Math.floor(s / 3600), Math.floor(s / 60) % 60].map(v => String(v).padStart(2, '0')).join(':')
    return metric(label, round(site.scale * weather[weekIndex(day + Math.floor(t / 86400))] * solarAt(s).power))
  })
  const weekPoints = Array.from({ length: 7 }, (_, i) => {
    const d = day + i - 6
    const date = new Date(anchor + d * dayMs + 8 * hourMs).toISOString().slice(5, 10)
    return metric(date, i === 6 ? site.day : round(site.scale * weather[weekIndex(d)] * fullDay / 10))
  })
  const breeze = Math.sin((day * 86400 + seconds) / 122)
  const temperature = Math.sin((seconds / 3600 - 8) * Math.PI / 12)
  return {
    power: site.power,
    fdData: group('数据一览', totals.map((v, i) => metric(periods[i] + '发电量', v, '万kWh'))),
    syData: group('收益', totals.map((v, i) => metric(periods[i] + '收益', round(v * tariff), '万元'))),
    otherData: group('容量', [metric('总装机容量', site.capacity, 'MW'), metric('集控接入容量', site.capacity / 10, '万kW')]),
    envData: group('今日环境数据', [metric('湿度', round(52 - 7 * temperature + breeze), '%'), metric('气压', round(944 + 1.4 * breeze), 'hPa'),
      metric('环境温度', round(8 + 6 * temperature + breeze / 4), '℃'), metric('风速', round(2.4 + 0.7 * breeze), 'm/s'), metric('风向', Math.round(61 + 12 * breeze), '°')]),
    devOpsData: group('运维数据', [metric('安全生产', 92 + day, '天'), metric('年利用小时', round(site.year * 10 / site.capacity), '小时'),
      metric('设备完好率', '100%'), metric('今日告警数量', 0, '次')]),
    socialBenefit: benefits(site.generation),
    powerChart: group('近30分钟有功功率', [{ label: '总有功功率', unit: 'MW', childs: powerPoints }]),
    energyChart: group('近七日发电量', [{ label: '日发电量', unit: '万kWh', childs: weekPoints }]),
  }
}
export function readSnapshot(now = Date.now()) {
  const clock = calendar(now)
  const sun = solarAt(clock.seconds)
  const sites = opening.map(site => reading(site, clock, sun))
  return { overview: makeOverview(sites, clock.day), station: stationData(sites[2], clock) }
}
