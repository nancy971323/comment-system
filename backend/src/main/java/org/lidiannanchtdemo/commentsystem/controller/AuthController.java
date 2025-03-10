package org.lidiannanchtdemo.commentsystem.controller;

import org.lidiannanchtdemo.commentsystem.dto.LoginDTO;
import org.lidiannanchtdemo.commentsystem.dto.RegisterDTO;
import org.lidiannanchtdemo.commentsystem.dto.UserDTO;
import org.lidiannanchtdemo.commentsystem.exception.AuthException;
import org.lidiannanchtdemo.commentsystem.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * 身份驗證控制器
 * 處理用戶註冊、登入和登出
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * 用戶註冊
     * @param registerDTO 包含用戶名、電子郵件和密碼的DTO
     * @return 包含用戶信息和認證令牌的回應
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody RegisterDTO registerDTO) {
        try {
            // 註冊用戶並生成令牌
            Map<String, Object> result = authService.registerUser(registerDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (AuthException e) {
            // 註冊失敗，返回錯誤訊息
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * 用戶登入
     * @param loginDTO 包含用戶名和密碼的DTO
     * @return 包含用戶信息和認證令牌的回應
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginDTO loginDTO) {
        try {
            // 驗證用戶憑證並生成令牌
            Map<String, Object> result = authService.authenticateUser(loginDTO);
            return ResponseEntity.ok(result);
        } catch (AuthException e) {
            // 登入失敗，返回錯誤訊息
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    /**
     * 用戶登出
     * @param token 認證令牌
     * @return 成功訊息
     */
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            authService.logoutUser(token);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "已成功登出");
            return ResponseEntity.ok(response);
        }
        
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", "登出失敗，沒有提供有效的認證令牌");
        return ResponseEntity.badRequest().body(errorResponse);
    }
}