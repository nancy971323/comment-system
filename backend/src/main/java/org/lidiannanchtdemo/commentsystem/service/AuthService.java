package org.lidiannanchtdemo.commentsystem.service;

import org.lidiannanchtdemo.commentsystem.dto.LoginDTO;
import org.lidiannanchtdemo.commentsystem.dto.RegisterDTO;
import org.lidiannanchtdemo.commentsystem.dto.UserDTO;
import org.lidiannanchtdemo.commentsystem.exception.AuthException;
import org.lidiannanchtdemo.commentsystem.model.User;
import org.lidiannanchtdemo.commentsystem.repository.UserRepository;
import org.lidiannanchtdemo.commentsystem.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 身份驗證服務
 * 處理用戶註冊、身份驗證和令牌管理
 */
@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private SessionService sessionService;

    /**
     * 註冊新用戶
     * @param registerDTO 包含用戶註冊信息的DTO
     * @return 包含用戶信息和認證令牌的Map
     * @throws AuthException 如果用戶名或電子郵件已被使用
     */
    public Map<String, Object> registerUser(RegisterDTO registerDTO) throws AuthException {
        // 檢查用戶名是否已存在
        if (userRepository.existsByUsername(registerDTO.getUsername())) {
            throw new AuthException("用戶名已被使用");
        }
        
        // 檢查電子郵件是否已存在
        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            throw new AuthException("電子郵件已被使用");
        }
        
        // 創建新用戶
        User newUser = new User();
        newUser.setUsername(registerDTO.getUsername());
        newUser.setEmail(registerDTO.getEmail());
        
        // 加密密碼
        String hashedPassword = PasswordUtil.hashPassword(registerDTO.getPassword());
        newUser.setPassword(hashedPassword);
        
        // 保存用戶
        User savedUser = userRepository.save(newUser);
        
        // 生成認證令牌
        String token = sessionService.createSession(savedUser);
        
        // 創建回應
        Map<String, Object> response = new HashMap<>();
        response.put("user", UserDTO.fromEntity(savedUser));
        response.put("token", token);
        
        return response;
    }

    /**
     * 驗證用戶憑證並登入
     * @param loginDTO 包含用戶登入信息的DTO
     * @return 包含用戶信息和認證令牌的Map
     * @throws AuthException 如果憑證無效
     */
    public Map<String, Object> authenticateUser(LoginDTO loginDTO) throws AuthException {
        // 查找用戶
        Optional<User> userOptional = userRepository.findByUsername(loginDTO.getUsername());
        
        // 檢查用戶是否存在並驗證密碼
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            
            // 驗證密碼
            if (PasswordUtil.checkPassword(loginDTO.getPassword(), user.getPassword())) {
                // 密碼正確，生成認證令牌
                String token = sessionService.createSession(user);
                
                // 創建回應
                Map<String, Object> response = new HashMap<>();
                response.put("user", UserDTO.fromEntity(user));
                response.put("token", token);
                
                return response;
            }
        }
        
        // 身份驗證失敗
        throw new AuthException("用戶名或密碼不正確");
    }

    /**
     * 用戶登出，使令牌失效
     * @param token 認證令牌
     */
    public void logoutUser(String token) {
        sessionService.invalidateSession(token);
    }
}