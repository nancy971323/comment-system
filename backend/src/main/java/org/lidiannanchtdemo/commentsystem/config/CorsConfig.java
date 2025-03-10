package org.lidiannanchtdemo.commentsystem.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

/**
 * 跨域資源共用 (CORS) 配置
 * 允許前端應用程式與後端API進行跨域通信
 */
@Configuration
public class CorsConfig {

    @Value("${cors.allowedOrigins}")
    private String allowedOrigins;

    @Value("${cors.allowedMethods}")
    private String allowedMethods;

    @Value("${cors.allowCredentials}")
    private boolean allowCredentials;

    @Value("${cors.maxAge}")
    private long maxAge;

    /**
     * 建立CORS過濾器
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        
        // 設置允許的源站
        String[] origins = allowedOrigins.split(",");
        Arrays.stream(origins).forEach(corsConfiguration::addAllowedOrigin);
        
        // 設置允許的HTTP方法
        String[] methods = allowedMethods.split(",");
        Arrays.stream(methods).forEach(corsConfiguration::addAllowedMethod);
        
        // 設置允許的請求頭
        corsConfiguration.addAllowedHeader("*");
        
        // 設置是否允許帶憑證
        corsConfiguration.setAllowCredentials(allowCredentials);
        
        // 設置預檢請求的有效期
        corsConfiguration.setMaxAge(maxAge);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsFilter(source);
    }
}