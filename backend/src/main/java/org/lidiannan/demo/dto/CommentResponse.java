package org.lidiannan.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.lidiannan.demo.model.Comment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {
    private Long id;
    private String content;
    private String username;
    private LocalDateTime createdAt;
    private Integer likes;
    private List<CommentResponse> replies = new ArrayList<>();
    
    // 用於轉換 Comment 實體到 Response DTO
    public static CommentResponse fromEntity(Comment comment) {
        CommentResponse response = new CommentResponse();
        response.setId(comment.getId());
        response.setContent(comment.getContent());
        response.setUsername(comment.getUser().getUsername());
        response.setCreatedAt(comment.getCreatedAt());
        response.setLikes(comment.getLikes());
        
        // 轉換回覆 (僅處理活躍的回覆)
        if (comment.getReplies() != null) {
            response.setReplies(comment.getReplies().stream()
                    .filter(Comment::getActive)
                    .map(CommentResponse::fromEntity)
                    .collect(Collectors.toList()));
        }
        
        return response;
    }
}