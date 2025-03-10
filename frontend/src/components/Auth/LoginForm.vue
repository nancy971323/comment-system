<template>
    <form @submit.prevent="handleSubmit" class="login-form">
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
  
      <button type="submit" class="submit-button" :disabled="isLoading">
        {{ isLoading ? '登入中...' : '登入' }}
      </button>
    </form>
  </template>
  
  <script>
  export default {
    name: 'LoginForm',
    data() {
      return {
        form: {
          username: '',
          password: ''
        },
        errors: {
          username: '',
          password: ''
        },
        isLoading: false
      }
    },
    methods: {
      validateForm() {
        let isValid = true
        this.errors = {
          username: '',
          password: ''
        }
  
        if (!this.form.username.trim()) {
          this.errors.username = '請輸入用戶名'
          isValid = false
        }
  
        if (!this.form.password) {
          this.errors.password = '請輸入密碼'
          isValid = false
        } else if (this.form.password.length < 6) {
          this.errors.password = '密碼至少需要6個字符'
          isValid = false
        }
  
        return isValid
      },
      
      async handleSubmit() {
        if (!this.validateForm()) {
          return
        }
  
        this.isLoading = true
        
        try {
          await this.$emit('login', {
            username: this.form.username.trim(),
            password: this.form.password
          })
          
          // 清空表單
          this.form = {
            username: '',
            password: ''
          }
        } catch (error) {
          this.$emit('login-error', error.message || '登入失敗')
        } finally {
          this.isLoading = false
        }
      }
    }
  }
  </script>
  
  <style scoped>
  .login-form {
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