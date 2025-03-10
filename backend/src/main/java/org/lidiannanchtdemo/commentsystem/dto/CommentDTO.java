package org.lidiannanchtdemo.commentsystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.lidiannanchtdemo.commentsystem.model.Comment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 留言數據傳輸對象
 * 用於在前後端之間傳輸留言數據
 */
public class CommentDTO {

    private Long id;
    
    @NotBlank(message = "留言內容不能為空")
    @Size(min = 1, max = 1000, message = "留言內容長度必須在1-1000個字符之間")
    private String content;
    
    private Long authorId;
    private String authorName;
    private Long parentId;
    private LocalDateTime createdAt;
    private int likeCount;
    private boolean liked;
    private List<CommentDTO> replies = new ArrayList<>();

    // 從Comment實體創建CommentDTO的靜態工廠方法
    public static CommentDTO fromEntity(Comment comment, Long currentUserId) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setAuthorId(comment.getAuthor().getId());
        dto.setAuthorName(comment.getAuthor().getUsername());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setLikeCount(comment.getLikeCount());
        
        // 設置當前用戶是否點讚過該留言
        if (currentUserId != null) {
            dto.setLiked(comment.isLikedBy(comment.getAuthor()));
        }
        
        // 設置父留言ID，如果有的話
        if (comment.getParent() != null) {
            dto.setParentId(comment.getParent().getId());
        }
        
        // 轉換回覆列表
        if (comment.getReplies() != null && !comment.getReplies().isEmpty()) {
            dto.setReplies(comment.getReplies().stream()
                    .map(reply -> CommentDTO.fromEntity(reply, currentUserId))
                    .collect(Collectors.toList()));
        }
        
        return dto;
    }

    // Getters and Setters
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public List<CommentDTO> getReplies() {
        return replies;
    }

    public void setReplies(List<CommentDTO> replies) {
        this.replies = replies;
    }
}