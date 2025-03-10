module.exports = {
    devServer: {
      proxy: {
        '/api': {
          target: 'http://localhost:8080',
          changeOrigin: true
        }
      }
    },
    // 預設輸出路徑設為後端的static資料夾
    outputDir: '../backend/src/main/resources/static',
    // 生產環境不產生sourcemap
    productionSourceMap: false
  }