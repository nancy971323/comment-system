package org.lidiannanchtdemo.commentsystem.exception;

/**
 * 身份驗證異常
 * 用於表示身份驗證過程中發生的錯誤
 */
public class AuthException extends RuntimeException {

    public AuthException(String message) {
        super(message);
    }

    public AuthException(String message, Throwable cause) {
        super(message, cause);
    }
}