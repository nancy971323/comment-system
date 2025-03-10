package org.lidiannanchtdemo.commentsystem.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * 密碼工具類
 * 提供密碼加密和驗證功能
 */
public class PasswordUtil {

    /**
     * 加密密碼
     * @param password 原始密碼
     * @return 加密後的密碼
     */
    public static String hashPassword(String password) {
        // 生成鹽並加密密碼
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    /**
     * 驗證密碼是否匹配
     * @param plainPassword 原始密碼
     * @param hashedPassword 加密後的密碼
     * @return 是否匹配
     */
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        // 驗證原始密碼是否與加密密碼匹配
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}