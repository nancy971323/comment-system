FROM openjdk:17-jdk-slim

WORKDIR /app

# 設置時區
ENV TZ=Asia/Taipei
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 複製 Maven 構建的 JAR 文件
COPY target/*.jar app.jar

# 指定應用程式對外暴露的端口
EXPOSE 8080

# 配置應用程式啟動命令
ENTRYPOINT ["java", "-jar", "/app/app.jar"]