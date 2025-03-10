package org.lidiannanchtdemo.commentsystem.controller;

import org.lidiannanchtdemo.commentsystem.dto.CommentDTO;
import org.lidiannanchtdemo.commentsystem.model.User;
import org.lidiannanchtdemo.commentsystem.service.CommentService;
import org.lidiannanchtdemo.commentsystem.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 留言控制器
 * 處理留言的創建、獲取、更新和刪除
 */
@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;
    
    @Autowired
    private SessionService sessionService;

    /**
     * 獲取當前用戶
     * 從Authorization頭部獲取令牌並查找對應的用戶
     */
    private User getCurrentUser(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return sessionService.getUserFromToken(token);
        }
        return null;
    }

    /**
     * 獲取所有留言
     * @return 留言列表
     */
    @GetMapping
    public ResponseEntity<List<CommentDTO>> getAllComments(
            @RequestHeader("Authorization") String authHeader) {
        User currentUser = getCurrentUser(authHeader);
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        List<CommentDTO> comments = commentService.getAllComments(currentUser.getId());
        return ResponseEntity.ok(comments);
    }

    /**
     * 創建新留言
     * @param commentDTO 包含留言內容的DTO
     * @return 創建的留言
     */
    @PostMapping
    public ResponseEntity<CommentDTO> createComment(
            @Valid @RequestBody CommentDTO commentDTO,
            @RequestHeader("Authorization") String authHeader) {
        User currentUser = getCurrentUser(authHeader);
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        CommentDTO createdComment = commentService.createComment(commentDTO.getContent(), currentUser.getId(), null);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

    /**
     * 回覆留言
     * @param parentId 父留言ID
     * @param commentDTO 包含回覆內容的DTO
     * @return 創建的回覆
     */
    @PostMapping("/{parentId}/reply")
    public ResponseEntity<CommentDTO> replyToComment(
            @PathVariable Long parentId,
            @Valid @RequestBody CommentDTO commentDTO,
            @RequestHeader("Authorization") String authHeader) {
        User currentUser = getCurrentUser(authHeader);
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        CommentDTO createdReply = commentService.createComment(commentDTO.getContent(), currentUser.getId(), parentId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReply);
    }

    /**
     * 點讚留言
     * @param commentId 留言ID
     * @return 成功訊息
     */
    @PostMapping("/{commentId}/like")
    public ResponseEntity<Map<String, String>> likeComment(
            @PathVariable Long commentId,
            @RequestHeader("Authorization") String authHeader) {
        User currentUser = getCurrentUser(authHeader);
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        commentService.likeComment(commentId, currentUser.getId());
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "已成功點讚");
        return ResponseEntity.ok(response);
    }

    /**
     * 刪除留言
     * @param commentId 留言ID
     * @return 成功訊息
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Map<String, String>> deleteComment(
            @PathVariable Long commentId,
            @RequestHeader("Authorization") String authHeader) {
        User currentUser = getCurrentUser(authHeader);
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        boolean deleted = commentService.deleteComment(commentId, currentUser.getId());
        
        if (deleted) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "留言已成功刪除");
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "無權刪除此留言或留言不存在");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
    }
}