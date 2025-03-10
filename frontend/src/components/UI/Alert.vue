<template>
    <div class="alert" :class="typeClass">
      <span class="message">{{ message }}</span>
      <button class="close-button" @click="close">&times;</button>
    </div>
  </template>
  
  <script>
  export default {
    name: 'SystemAlert',
    props: {
      message: {
        type: String,
        required: true
      },
      type: {
        type: String,
        default: 'info',
        validator: value => ['info', 'success', 'warning', 'error'].includes(value)
      }
    },
    computed: {
      typeClass() {
        return `alert-${this.type}`
      }
    },
    methods: {
      close() {
        this.$emit('close')
      }
    }
  }
  </script>
  
  <style scoped>
  .alert {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 12px 15px;
    border-radius: 4px;
    margin-bottom: 20px;
    font-size: 15px;
  }
  
  .alert-info {
    background-color: #e1f5fe;
    color: #0288d1;
    border-left: 4px solid #0288d1;
  }
  
  .alert-success {
    background-color: #e8f5e9;
    color: #4caf50;
    border-left: 4px solid #4caf50;
  }
  
  .alert-warning {
    background-color: #fff8e1;
    color: #ff8f00;
    border-left: 4px solid #ff8f00;
  }
  
  .alert-error {
    background-color: #ffebee;
    color: #e53935;
    border-left: 4px solid #e53935;
  }
  
  .message {
    flex-grow: 1;
  }
  
  .close-button {
    background: none;
    border: none;
    font-size: 18px;
    cursor: pointer;
    opacity: 0.7;
    transition: opacity 0.2s;
    padding: 0 0 0 15px;
  }
  
  .close-button:hover {
    opacity: 1;
  }
  </style>