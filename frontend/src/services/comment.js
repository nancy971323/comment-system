import { getToken } from '../utils/storage'

// 封裝API請求方法
const apiRequest = async (url, method = 'GET', data = null) => {
  const headers = {
    'Content-Type': 'application/json'
  }

  // 加入認證令牌
  const token = getToken()
  if (token) {
    headers['Authorization'] = `Bearer ${token}`
  }

  const config = {
    method,
    headers,
    credentials: 'include'
  }

  if (data) {
    config.body = JSON.stringify(data)
  }

  const response = await fetch(url, config)
  
  if (!response.ok) {
    const errorData = await response.json().catch(() => ({
      message: `HTTP error! status: ${response.status}`
    }))
    throw new Error(errorData.message || 'API請求失敗')
  }

  return response.json()
}

// 獲取所有留言
export const getComments = async () => {
  return apiRequest('/api/comments')
}

// 創建新留言
export const createComment = async (content) => {
  return apiRequest('/api/comments', 'POST', { content })
}

// 回覆留言
export const replyComment = async (parentId, content) => {
  return apiRequest(`/api/comments/${parentId}/reply`, 'POST', { content })
}

// 給留言點讚
export const likeComment = async (commentId) => {
  return apiRequest(`/api/comments/${commentId}/like`, 'POST')
}

// 刪除留言
export const deleteComment = async (commentId) => {
  return apiRequest(`/api/comments/${commentId}`, 'DELETE')
}