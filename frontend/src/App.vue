<template>
    <div class="app-container">
      <NavBar 
        :isLoggedIn="isLoggedIn" 
        :username="currentUser ? currentUser.username : ''" 
        @logout="handleLogout" 
      />
      
      <div class="content">
        <SystemAlert 
          v-if="alert.show" 
          :message="alert.message" 
          :type="alert.type" 
          @close="clearAlert" 
        />
        
        <div v-if="!isLoggedIn" class="auth-container">
          <div class="auth-toggle">
            <button 
              :class="{ active: showLogin }" 
              @click="showLogin = true"
            >登入</button>
            <button 
              :class="{ active: !showLogin }" 
              @click="showLogin = false"
            >註冊</button>
          </div>
          
          <LoginForm 
            v-if="showLogin" 
            @login="handleLogin" 
            @login-error="showAlert" 
          />
          <RegisterForm 
            v-else 
            @register="handleRegister" 
            @register-error="showAlert" 
          />
        </div>
        
        <div v-else class="comments-container">
          <CommentForm @comment-added="loadComments" />
          <CommentList 
            :comments="comments" 
            :currentUserId="currentUser.id" 
            @comment-action="handleCommentAction" 
          />
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import NavBar from './components/UI/NavBar.vue'
  import SystemAlert from './components/UI/Alert.vue'
  import LoginForm from './components/Auth/LoginForm.vue'
  import RegisterForm from './components/Auth/RegisterForm.vue'
  import CommentForm from './components/Comment/CommentForm.vue'
  import CommentList from './components/Comment/CommentList.vue'
  import { login, register, logout } from './services/auth'
  import { getComments, likeComment, replyComment, deleteComment } from './services/comment'
  import { getStoredUser, clearUserData } from './utils/storage'
  
  export default {
    name: 'App',
    components: {
      NavBar,
      SystemAlert,
      LoginForm,
      RegisterForm,
      CommentForm,
      CommentList
    },
    data() {
      return {
        showLogin: true,
        currentUser: null,
        comments: [],
        alert: {
          show: false,
          message: '',
          type: 'info'
        }
      }
    },
    computed: {
      isLoggedIn() {
        return this.currentUser !== null
      }
    },
    created() {
      // 檢查本地存儲是否有用戶信息
      const storedUser = getStoredUser()
      if (storedUser) {
        this.currentUser = storedUser
        this.loadComments()
      }
    },
    methods: {
      async handleLogin(credentials) {
        try {
          const user = await login(credentials)
          this.currentUser = user
          this.loadComments()
          this.showAlert('登入成功', 'success')
        } catch (error) {
          console.error('Login error:', error)
          this.showAlert(error.message || '登入失敗，請稍後再試', 'error')
        }
      },
      async handleRegister(userData) {
        try {
          const user = await register(userData)
          this.currentUser = user
          this.showLogin = true
          this.showAlert('註冊成功，已自動登入', 'success')
          this.loadComments()
        } catch (error) {
          console.error('Register error:', error)
          this.showAlert(error.message || '註冊失敗，請稍後再試', 'error')
        }
      },
      async handleLogout() {
        try {
          await logout()
          this.currentUser = null
          this.comments = []
          clearUserData()
          this.showAlert('已成功登出', 'info')
        } catch (error) {
          console.error('Logout error:', error)
        }
      },
      async loadComments() {
        if (!this.isLoggedIn) return
        
        try {
          this.comments = await getComments()
        } catch (error) {
          console.error('Error loading comments:', error)
          this.showAlert('載入留言失敗', 'error')
        }
      },
      async handleCommentAction(action, commentId, content) {
        if (!this.isLoggedIn) return
        
        try {
          switch (action) {
            case 'like':
              await likeComment(commentId)
              break
            case 'reply':
              await replyComment(commentId, content)
              break
            case 'delete':
              await deleteComment(commentId)
              break
            default:
              console.warn('Unknown action:', action)
              return
          }
          
          // 刷新留言列表
          this.loadComments()
        } catch (error) {
          console.error(`Error performing ${action}:`, error)
          this.showAlert(`操作失敗: ${error.message || '發生未知錯誤'}`, 'error')
        }
      },
      showAlert(message, type = 'info') {
        this.alert = {
          show: true,
          message,
          type
        }
        
        // 3秒後自動關閉
        setTimeout(() => {
          this.clearAlert()
        }, 3000)
      },
      clearAlert() {
        this.alert.show = false
      }
    }
  }
  </script>
  
  <style>
  body {
    margin: 0;
    font-family: Arial, sans-serif;
    background-color: #f5f5f5;
  }
  
  .app-container {
    max-width: 900px;
    margin: 0 auto;
    padding: 20px;
  }
  
  .content {
    margin-top: 20px;
  }
  
  .auth-container {
    max-width: 400px;
    margin: 30px auto;
    background: white;
    border-radius: 8px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    padding: 20px;
  }
  
  .auth-toggle {
    display: flex;
    margin-bottom: 20px;
    border-bottom: 1px solid #eee;
  }
  
  .auth-toggle button {
    flex: 1;
    background: none;
    border: none;
    padding: 10px;
    font-size: 16px;
    cursor: pointer;
    color: #333;
  }
  
  .auth-toggle button.active {
    font-weight: bold;
    border-bottom: 2px solid #4CAF50;
    color: #4CAF50;
  }
  
  .comments-container {
    background: white;
    border-radius: 8px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    padding: 20px;
  }
  </style>