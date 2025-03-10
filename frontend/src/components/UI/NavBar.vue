<template>
    <nav class="navbar">
      <div class="brand">
        <h1>留言系統</h1>
      </div>
      
      <div class="user-section">
        <div v-if="isLoggedIn" class="user-info">
          <span class="welcome-text">歡迎, {{ username }}</span>
          <button class="logout-button" @click="logout">登出</button>
        </div>
      </div>
    </nav>
  </template>
  
  <script>
  export default {
    name: 'NavBar',
    props: {
      isLoggedIn: {
        type: Boolean,
        required: true
      },
      username: {
        type: String,
        default: ''
      }
    },
    methods: {
      logout() {
        this.$emit('logout')
      }
    }
  }
  </script>
  
  <style scoped>
  .navbar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 15px 0;
    border-bottom: 1px solid #eee;
  }
  
  .brand h1 {
    margin: 0;
    font-size: 24px;
    color: #4CAF50;
  }
  
  .user-section {
    display: flex;
    align-items: center;
  }
  
  .user-info {
    display: flex;
    align-items: center;
    gap: 15px;
  }
  
  .welcome-text {
    font-weight: 500;
  }
  
  .logout-button {
    background-color: #f5f5f5;
    border: none;
    padding: 8px 15px;
    border-radius: 4px;
    cursor: pointer;
    color: #333;
    font-size: 14px;
    transition: background-color 0.2s;
  }
  
  .logout-button:hover {
    background-color: #e0e0e0;
  }
  </style>