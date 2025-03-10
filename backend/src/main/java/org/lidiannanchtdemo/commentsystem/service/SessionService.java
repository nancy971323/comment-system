package org.lidiannanchtdemo.commentsystem.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.lidiannanchtdemo.commentsystem.model.User;
import org.lidiannanchtdemo.commentsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 會話服務
 * 處理用戶會話令牌的創建、驗證和管理
 */
@Service
public class SessionService {

    @Autowired
    private UserRepository userRepository;
    
    @Value("${auth.token.secret}")
    private String tokenSecret;
    
    @Value("${auth.token.expiration}")
    private long tokenExpiration;
    
    private final Key key;
    
    // 在構造函數中初始化密鑰
    public SessionService(@Value("${auth.token.secret}") String tokenSecret) {
        this.tokenSecret = tokenSecret;
        // 使用至少256位的密鑰來滿足HMAC-SHA256的要求
        this.key = Keys.hmacShaKeyFor(tokenSecret.getBytes());
    }

    /**
     * 為用戶創建會話令牌
     * @param user 用戶實體
     * @return 會話令牌
     */
    public String createSession(User user) {
        // 設置令牌過期時間
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + tokenExpiration);
        
        // 設置令牌聲明
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("username", user.getUsername());
        
        // 生成令牌
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 驗證令牌是否有效
     * @param token 會話令牌
     * @return 是否有效
     */
    public boolean validateToken(String token) {
        try {
            // 解析令牌並檢查是否過期
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            // 令牌無效或已過期
            return false;
        }
    }

    /**
     * 從令牌中獲取用戶ID
     * @param token 會話令牌
     * @return 用戶ID，如果令牌無效則返回null
     */
    public Long getUserIdFromToken(String token) {
        try {
            // 解析令牌
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            
            // 獲取用戶ID
            return claims.get("userId", Long.class);
        } catch (JwtException e) {
            // 令牌無效或已過期
            return null;
        }
    }

    /**
     * 從令牌中獲取用戶
     * @param token 會話令牌
     * @return 用戶實體，如果令牌無效或用戶不存在則返回null
     */
    public User getUserFromToken(String token) {
        Long userId = getUserIdFromToken(token);
        if (userId != null) {
            Optional<User> userOptional = userRepository.findById(userId);
            return userOptional.orElse(null);
        }
        return null;
    }

    /**
     * 使令牌失效（登出）
     * 注意：JWT本身是無狀態的，無法直接使其失效
     * 在實際應用中，可以使用黑名單或改變密鑰等方式處理
     * 這裡為了簡化，僅提供接口
     * @param token 會話令牌
     */
    public void invalidateSession(String token) {
        // 在實際應用中，可以將令牌添加到黑名單
        // 或者更新用戶的最後登出時間
    }
}