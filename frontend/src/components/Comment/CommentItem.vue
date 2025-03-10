<template>
    <div class="comment-item" :class="{ 'reply': isReply }">
      <div class="comment-header">
        <span class="username">{{ comment.authorName }}</span>
        <span class="date">{{ formatDate(comment.createdAt) }}</span>
      </div>
      
      <div class="comment-content">
        {{ comment.content }}
      </div>
      
      <div class="comment-actions">
        <button 
          class="action-button like-button" 
          @click="handleLike"
          :class="{ 'liked': comment.liked }"
        >
          {{ comment.liked ? '已讚' : '讚' }} ({{ comment.likeCount }})
        </button>
        
        <button 
          class="action-button reply-button" 
          @click="showReplyForm = !showReplyForm"
        >
          回覆
        </button>
        
        <button 
          v-if="isAuthor" 
          class="action-button delete-button" 
          @click="confirmDelete"
        >
          刪除
        </button>
      </div>
      
      <!-- 回覆表單 -->
      <div v-if="showReplyForm" class="reply-form">
        <textarea 
          v-model="replyContent" 
          placeholder="寫下你的回覆..."
          :class="{ 'invalid': replyError }"
        ></textarea>
        <div v-if="replyError" class="error-message">{{ replyError }}</div>
        <div class="reply-actions">
          <button 
            class="cancel-button" 
            @click="cancelReply"
          >
            取消
          </button>
          <button 
            class="submit-button" 
            @click="submitReply" 
            :disabled="isSubmitting"
          >
            {{ isSubmitting ? '提交中...' : '提交回覆' }}
          </button>
        </div>
      </div>
      
      <!-- 子回覆列表 -->
      <div v-if="comment.replies && comment.replies.length > 0" class="replies">
        <div 
          v-for="reply in comment.replies" 
          :key="reply.id" 
          class="reply-item"
        >
          <CommentItem 
            :comment="reply" 
            :currentUserId="currentUserId" 
            :isReply="true" 
            @action="handleAction"
          />
        </div>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: 'CommentItem',
    props: {
      comment: {
        type: Object,
        required: true
      },
      currentUserId: {
        type: Number,
        required: true
      },
      isReply: {
        type: Boolean,
        default: false
      }
    },
    data() {
      return {
        showReplyForm: false,
        replyContent: '',
        replyError: '',
        isSubmitting: false
      }
    },
    computed: {
      isAuthor() {
        return this.comment.authorId === this.currentUserId
      }
    },
    methods: {
      formatDate(dateString) {
        const date = new Date(dateString)
        return date.toLocaleString()
      },
      
      handleLike() {
        this.$emit('action', 'like', this.comment.id)
      },
      
      validateReply() {
        this.replyError = ''
        
        if (!this.replyContent.trim()) {
          this.replyError = '回覆內容不能為空'
          return false
        }
        
        if (this.replyContent.length > 500) {
          this.replyError = '回覆不能超過500字'
          return false
        }
        
        return true
      },
      
      async submitReply() {
        if (!this.validateReply()) {
          return
        }
        
        this.isSubmitting = true
        
        try {
          await this.$emit('action', 'reply', this.comment.id, this.replyContent.trim())
          this.replyContent = ''
          this.showReplyForm = false
        } catch (error) {
          console.error('Failed to submit reply:', error)
          this.replyError = error.message || '回覆失敗，請稍後再試'
        } finally {
          this.isSubmitting = false
        }
      },
      
      cancelReply() {
        this.showReplyForm = false
        this.replyContent = ''
        this.replyError = ''
      },
      
      confirmDelete() {
        if (confirm('確定要刪除這則留言嗎？')) {
          this.$emit('action', 'delete', this.comment.id)
        }
      },
      
      handleAction(action, commentId, content) {
        // 將操作傳遞給父組件
        this.$emit('action', action, commentId, content)
      }
    }
  }
  </script>
  
  <style scoped>
  .comment-item {
    border: 1px solid #eee;
    border-radius: 8px;
    padding: 15px;
    margin-bottom: 15px;
    background-color: #fff;
  }
  
  .comment-item.reply {
    border-left: 3px solid #4CAF50;
    margin-left: 20px;
    margin-top: 10px;
  }
  
  .comment-header {
    display: flex;
    justify-content: space-between;
    margin-bottom: 10px;
  }
  
  .username {
    font-weight: bold;
    color: #333;
  }
  
  .date {
    color: #777;
    font-size: 14px;
  }
  
  .comment-content {
    margin-bottom: 15px;
    line-height: 1.5;
    word-break: break-word;
  }
  
  .comment-actions {
    display: flex;
    gap: 10px;
  }
  
  .action-button {
    background: none;
    border: none;
    color: #666;
    cursor: pointer;
    font-size: 14px;
    padding: 5px 10px;
    border-radius: 4px;
  }
  
  .action-button:hover {
    background-color: #f0f0f0;
  }
  
  .like-button.liked {
    color: #4CAF50;
    font-weight: bold;
  }
  
  .delete-button {
    color: #f44336;
  }
  
  .reply-form {
    margin-top: 15px;
    padding-top: 15px;
    border-top: 1px solid #eee;
  }
  
  textarea {
    width: 100%;
    padding: 10px;
    border: 1px solid #ddd;
    border-radius: 4px;
    font-size: 14px;
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
  
  .reply-actions {
    display: flex;
    justify-content: flex-end;
    gap: 10px;
  }
  
  .cancel-button {
    background-color: #f1f1f1;
    color: #333;
    border: none;
    padding: 8px 15px;
    border-radius: 4px;
    cursor: pointer;
  }
  
  .submit-button {
    background-color: #4CAF50;
    color: white;
    border: none;
    padding: 8px 15px;
    border-radius: 4px;
    cursor: pointer;
  }
  
  .submit-button:disabled {
    background-color: #cccccc;
    cursor: not-allowed;
  }
  
  .replies {
    margin-top: 15px;
    padding-top: 10px;
    border-top: 1px dashed #eee;
  }
  </style>