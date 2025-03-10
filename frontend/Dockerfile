FROM node:20-alpine as build-stage

WORKDIR /app

# 複製 package.json 和 package-lock.json
COPY package*.json ./

# 安裝依賴
RUN npm install

# 複製專案文件
COPY . .

# 構建專案
RUN npm run build

# 生產環境階段
FROM nginx:stable-alpine as production-stage

# 複製構建結果到 Nginx 服務目錄
COPY --from=build-stage /app/dist /usr/share/nginx/html

# 複製自定義 Nginx 配置
COPY nginx.conf /etc/nginx/conf.d/default.conf

# 指定對外暴露的端口
EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]