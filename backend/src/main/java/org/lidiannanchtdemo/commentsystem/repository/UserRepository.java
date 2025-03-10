package org.lidiannanchtdemo.commentsystem.repository;

import org.lidiannanchtdemo.commentsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 用戶數據訪問接口
 * 提供用戶CRUD操作和自定義查詢方法
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * 根據用戶名查找用戶
     */
    Optional<User> findByUsername(String username);
    
    /**
     * 根據電子郵件查找用戶
     */
    Optional<User> findByEmail(String email);
    
    /**
     * 檢查用戶名是否已存在
     */
    boolean existsByUsername(String username);
    
    /**
     * 檢查電子郵件是否已存在
     */
    boolean existsByEmail(String email);
}