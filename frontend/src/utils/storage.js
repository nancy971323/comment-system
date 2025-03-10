// 本地儲存用戶資訊
export const saveUserData = (user, token) => {
    localStorage.setItem('user', JSON.stringify(user))
    localStorage.setItem('token', token)
  }
  
  // 獲取儲存的用戶資訊
  export const getStoredUser = () => {
    const userStr = localStorage.getItem('user')
    return userStr ? JSON.parse(userStr) : null
  }
  
  // 獲取儲存的認證令牌
  export const getToken = () => {
    return localStorage.getItem('token')
  }
  
  // 清除所有用戶資料
  export const clearUserData = () => {
    localStorage.removeItem('user')
    localStorage.removeItem('token')
  }