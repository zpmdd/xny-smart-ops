// import Cookies from 'js-cookie'

const TokenKey = 'user-token'
const IdKey = 'user-login_id'
const NameKey = 'user-name'
const AccountKey = 'account'

export function getToken() {
  return localStorage.getItem(TokenKey)
}

export function setToken(token) {
  return localStorage.setItem(TokenKey, token)
}

export function removeToken() {
  return localStorage.removeItem(TokenKey)
}

export function getLoginId() {
  return localStorage.getItem(IdKey)
}

export function setLoginId(id) {
  return localStorage.setItem(IdKey, id)
}

export function removeLoginId() {
  return localStorage.removeItem(IdKey)
}

export function getName() {
  return localStorage.getItem(NameKey)
}

export function setName(name) {
  return localStorage.setItem(NameKey, name)
}

export function removeName() {
  return localStorage.removeItem(NameKey)
}

export function getAccount() {
  return localStorage.getItem(AccountKey)
}

export function setAccount(name) {
  return localStorage.setItem(AccountKey, name)
}

export function removeAccount() {
  return localStorage.removeItem(AccountKey)
}