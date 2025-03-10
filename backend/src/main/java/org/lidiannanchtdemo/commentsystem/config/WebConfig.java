package org.lidiannanchtdemo.commentsystem.config;

import org.lidiannanchtdemo.commentsystem.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

/**
 * Web MVC配置
 * 設置身份驗證攔截器以保護API端點
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);
    
    @Autowired
    private SessionService sessionService;
    
    /**
     * 設置身份驗證攔截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor())
                .addPathPatterns("/api/**")  // 保護所有API路徑
                .excludePathPatterns(
                        "/api/auth/login",   // 排除登入路徑
                        "/api/auth/register" // 排除註冊路徑
                );
    }
    
    /**
     * 身份驗證攔截器
     * 檢查HTTP請求頭部中的身份驗證令牌
     */
    private class AuthInterceptor implements HandlerInterceptor {
        
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            // 如果是OPTIONS請求，允許通過（預檢請求）
            if (request.getMethod().equals("OPTIONS")) {
                return true;
            }
            
            // 從請求頭中獲取授權令牌
            String authHeader = request.getHeader("Authorization");
            
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                // 提取令牌
                String token = authHeader.substring(7);
                
                // 驗證令牌
                boolean isValid = sessionService.validateToken(token);
                
                if (isValid) {
                    // 令牌有效，允許請求
                    return true;
                }
            }
            
            // 令牌無效或不存在，返回401未授權
            logger.warn("Unauthorized access attempt: {}", request.getRequestURI());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
    }
}
