<template>
    <div class="comment-list">
      <h3>留言列表</h3>
      
      <div v-if="comments.length === 0" class="empty-state">
        目前沒有留言，成為第一個發表留言的人！
      </div>
      
      <div v-else>
        <CommentItem 
          v-for="comment in topLevelComments" 
          :key="comment.id"
          :comment="comment"
          :currentUserId="currentUserId"
          @action="handleCommentAction"
        />
      </div>
    </div>
  </template>
  
  <script>
  import CommentItem from './CommentItem.vue'
  
  export default {
    name: 'CommentList',
    components: {
      CommentItem
    },
    props: {
      comments: {
        type: Array,
        required: true
      },
      currentUserId: {
        type: Number,
        required: true
      }
    },
    computed: {
      // 篩選出頂層留言（非回覆）
      topLevelComments() {
        return this.comments.filter(comment => !comment.parentId)
      }
    },
    methods: {
      handleCommentAction(action, commentId, content) {
        this.$emit('comment-action', action, commentId, content)
      }
    }
  }
  </script>
  
  <style scoped>
  .comment-list {
    margin-top: 30px;
  }
  
  h3 {
    margin-top: 0;
    margin-bottom: 20px;
    color: #333;
  }
  
  .empty-state {
    padding: 30px;
    text-align: center;
    background-color: #f9f9f9;
    border: 1px dashed #ddd;
    border-radius: 8px;
    color: #777;
  }
  </style>