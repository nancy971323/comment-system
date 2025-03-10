<template>
    <div class="comment-form-container">
      <h3>發表留言</h3>
      <form @submit.prevent="submitComment" class="comment-form">
        <textarea
          v-model="content"
          placeholder="分享您的想法..."
          :class="{ 'invalid': errorMessage }"
          rows="4"
        ></textarea>
        <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>
        <button type="submit" :disabled="isSubmitting">
          {{ isSubmitting ? '發送中...' : '發送留言' }}
        </button>
      </form>
    </div>
  </template>
  
  <script>
  import { createComment } from '../../services/comment'
  
  export default {
    name: 'CommentForm',
    data() {
      return {
        content: '',
        isSubmitting: false,
        errorMessage: ''
      }
    },
    methods: {
      validateComment() {
        this.errorMessage = ''
        
        if (!this.content.trim()) {
          this.errorMessage = '留言內容不能為空'
          return false
        }
        
        if (this.content.length > 1000) {
          this.errorMessage = '留言不能超過1000字'
          return false
        }
        
        return true
      },
      
      async submitComment() {
        if (!this.validateComment()) {
          return
        }
        
        this.isSubmitting = true
        
        try {
          await createComment(this.content.trim())
          this.content = '' // 清空輸入框
          this.$emit('comment-added')
        } catch (error) {
          console.error('Failed to submit comment:', error)
          this.errorMessage = error.message || '發送留言失敗，請稍後再試'
        } finally {
          this.isSubmitting = false
        }
      }
    }
  }
  </script>
  
  <style scoped>
  .comment-form-container {
    margin-bottom: 30px;
  }
  
  h3 {
    margin-top: 0;
    margin-bottom: 15px;
    color: #333;
  }
  
  .comment-form {
    display: flex;
    flex-direction: column;
  }
  
  textarea {
    width: 100%;
    padding: 12px;
    border: 1px solid #ddd;
    border-radius: 4px;
    font-size: 16px;
    resize: vertical;
    margin-bottom: 10px;
    font-family: inherit;
  }
  
  textarea.invalid {
    border-color: #ff6b6b;
  }
  
  .error-message {
    color: #ff6b6b;
    font-size: 14px;
    margin-bottom: 10px;
  }
  
  button {
    align-self: flex-end;
    padding: 10px 20px;
    background-color: #4CAF50;
    color: white;
    border: none;
    border-radius: 4px;
    font-size: 15px;
    cursor: pointer;
  }
  
  button:hover {
    background-color: #45a049;
  }
  
  button:disabled {
    background-color: #cccccc;
    cursor: not-allowed;
  }
  </style>