<template>
    <form @submit.prevent="handleSubmit" class="register-form">
      <div class="form-group">
        <label for="username">用戶名</label>
        <input 
          type="text" 
          id="username" 
          v-model="form.username" 
          required 
          :class="{ 'invalid': errors.username }"
        />
        <div v-if="errors.username" class="error-message">{{ errors.username }}</div>
      </div>
  
      <div class="form-group">
        <label for="email">電子郵件</label>
        <input 
          type="email" 
          id="email" 
          v-model="form.email" 
          required 
          :class="{ 'invalid': errors.email }"
        />
        <div v-if="errors.email" class="error-message">{{ errors.email }}</div>
      </div>
      
      <div class="form-group">
        <label for="password">密碼</label>
        <input 
          type="password" 
          id="password" 
          v-model="form.password" 
          required 
          :class="{ 'invalid': errors.password }"
        />
        <div v-if="errors.password" class="error-message">{{ errors.password }}</div>
      </div>
  
      <div class="form-group">
        <label for="confirmPassword">確認密碼</label>
        <input 
          type="password" 
          id="confirmPassword" 
          v-model="form.confirmPassword" 
          required 
          :class="{ 'invalid': errors.confirmPassword }"
        />
        <div v-if="errors.confirmPassword" class="error-message">{{ errors.confirmPassword }}</div>
      </div>
  
      <button type="submit" class="submit-button" :disabled="isLoading">
        {{ isLoading ? '註冊中...' : '註冊' }}
      </button>
    </form>
  </template>
  
  <script>
  export default {
    name: 'RegisterForm',
    data() {
      return {
        form: {
          username: '',
          email: '',
          password: '',
          confirmPassword: ''
        },
        errors: {
          username: '',
          email: '',
          password: '',
          confirmPassword: ''
        },
        isLoading: false
      }
    },
    methods: {
      validateForm() {
        let isValid = true
        this.errors = {
          username: '',
          email: '',
          password: '',
          confirmPassword: ''
        }
  
        // 驗證用戶名
        if (!this.form.username.trim()) {
          this.errors.username = '請輸入用戶名'
          isValid = false
        } else if (this.form.username.length < 3) {
          this.errors.username = '用戶名至少需要3個字符'
          isValid = false
        }
  
        // 驗證電子郵件
        if (!this.form.email.trim()) {
          this.errors.email = '請輸入電子郵件'
          isValid = false
        } else if (!this.isValidEmail(this.form.email)) {
          this.errors.email = '請輸入有效的電子郵件地址'
          isValid = false
        }
  
        // 驗證密碼
        if (!this.form.password) {
          this.errors.password = '請輸入密碼'
          isValid = false
        } else if (this.form.password.length < 6) {
          this.errors.password = '密碼至少需要6個字符'
          isValid = false
        }
  
        // 驗證確認密碼
        if (this.form.password !== this.form.confirmPassword) {
          this.errors.confirmPassword = '兩次輸入的密碼不一致'
          isValid = false
        }
  
        return isValid
      },
  
      isValidEmail(email) {
        const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
        return re.test(email)
      },
      
      async handleSubmit() {
        if (!this.validateForm()) {
          return
        }
  
        this.isLoading = true
        
        try {
          await this.$emit('register', {
            username: this.form.username.trim(),
            email: this.form.email.trim(),
            password: this.form.password
          })
          
          // 清空表單
          this.form = {
            username: '',
            email: '',
            password: '',
            confirmPassword: ''
          }
        } catch (error) {
          this.$emit('register-error', error.message || '註冊失敗')
        } finally {
          this.isLoading = false
        }
      }
    }
  }
  </script>
  
  <style scoped>
  .register-form {
    width: 100%;
  }
  
  .form-group {
    margin-bottom: 15px;
  }
  
  label {
    display: block;
    margin-bottom: 5px;
    font-weight: bold;
  }
  
  input {
    width: 100%;
    padding: 10px;
    border: 1px solid #ddd;
    border-radius: 4px;
    font-size: 16px;
  }
  
  input.invalid {
    border-color: #ff6b6b;
  }
  
  .error-message {
    color: #ff6b6b;
    font-size: 14px;
    margin-top: 5px;
  }
  
  .submit-button {
    width: 100%;
    padding: 12px;
    background-color: #4CAF50;
    color: white;
    border: none;
    border-radius: 4px;
    font-size: 16px;
    cursor: pointer;
    margin-top: 10px;
  }
  
  .submit-button:hover {
    background-color: #45a049;
  }
  
  .submit-button:disabled {
    background-color: #cccccc;
    cursor: not-allowed;
  }
  </style>