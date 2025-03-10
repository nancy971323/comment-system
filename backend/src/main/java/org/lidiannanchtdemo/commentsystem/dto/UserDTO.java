package org.lidiannanchtdemo.commentsystem.dto;

import java.time.LocalDateTime;

/**
 * 用戶數據傳輸對象
 * 用於向客戶端返回用戶信息，不包含敏感數據如密碼
 */
public class UserDTO {

    private Long id;
    private String username;
    private String email;
    private LocalDateTime createdAt;

    // 從User實體創建UserDTO的靜態工廠方法
    public static UserDTO fromEntity(org.lidiannanchtdemo.commentsystem.model.User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }

    // Getters and Setters
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}