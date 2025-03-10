import { saveUserData, getToken, clearUserData } from '../utils/storage'

// 封裝API請求方法
const apiRequest = async (url, method = 'GET', data = null) => {
  const headers = {
    'Content-Type': 'application/json'
  }

  // 如果有登入令牌，加入Authorization頭
  const token = getToken()
  if (token) {
    headers['Authorization'] = `Bearer ${token}`
  }

  const config = {
    method,
    headers,
    credentials: 'include' // 包含cookie
  }

  if (data) {
    config.body = JSON.stringify(data)
  }

  const response = await fetch(url, config)
  
  // 檢查回應狀態
  if (!response.ok) {
    const errorData = await response.json().catch(() => ({
      message: `HTTP error! status: ${response.status}`
    }))
    throw new Error(errorData.message || 'API請求失敗')
  }

  return response.json()
}

// 用戶登入
export const login = async (credentials) => {
  try {
    const response = await apiRequest('/api/auth/login', 'POST', credentials)
    const { user, token } = response
    
    if (user && token) {
      saveUserData(user, token)
      return user
    }
    
    throw new Error('登入回應缺少必要資訊')
  } catch (error) {
    console.error('Login error:', error)
    throw error
  }
}

// 用戶註冊
export const register = async (userData) => {
  try {
    const response = await apiRequest('/api/auth/register', 'POST', userData)
    const { user, token } = response
    
    if (user && token) {
      saveUserData(user, token)
      return user
    }
    
    throw new Error('註冊回應缺少必要資訊')
  } catch (error) {
    console.error('Register error:', error)
    throw error
  }
}

// 用戶登出
export const logout = async () => {
  try {
    await apiRequest('/api/auth/logout', 'POST')
    clearUserData()
  } catch (error) {
    console.error('Logout error:', error)
    // 即使API失敗，也清除本地存儲
    clearUserData()
    throw error
  }
}