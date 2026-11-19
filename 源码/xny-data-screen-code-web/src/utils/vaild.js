/**
 * Validate account
 * @param {string} value - The value to be validated
 * @returns {boolean} - True if the value is valid, false otherwise
 */
export const vailtAccount = (value) => {
  let reg = /^[A-Za-z0-9]+$/
  return reg.test(value)
}

/**
 * Validate password
 * @param {string} value - The value to be validated
 * @returns {boolean} - True if the value is valid, false otherwise
 */
export const vailtPassword = (value) => {
  let reg = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{6,18}$/
  return reg.test(value)
}