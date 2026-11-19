export const vailtAccount = (value) => {
  let reg = /^[A-Za-z0-9]+$/
  return reg.test(value)
}
export const vailtPassword = (value) => {
  let reg = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{6,18}$/
  return reg.test(value)
}
