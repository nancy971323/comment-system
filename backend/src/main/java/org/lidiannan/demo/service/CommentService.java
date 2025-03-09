package org.lidiannan.demo.service;

import lombok.RequiredArgsConstructor;
import org.lidiannan.demo.dto.CommentRequest;
import org.lidiannan.demo.dto.CommentResponse;
import org.lidiannan.demo.model.Comment;
import org.lidiannan.demo.model.User;
import org.lidiannan.demo.repository.CommentRepository;
import org.lidiannan.demo.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    /**
     * 獲取所有頂層留言
     */
    @Transactional(readOnly = true)
    public List<CommentResponse> getAllComments() {
        List<Comment> comments = commentRepository.findAllMainComments();
        return comments.stream()
                .map(CommentResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 建立新留言
     */
    @Transactional
    public CommentResponse createComment(CommentRequest request, Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setUser(user);
        
        // 處理回覆
        if (request.getParentId() != null) {
            Comment parentComment = commentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new EntityNotFoundException("Parent comment not found"));
            comment.setParentComment(parentComment);
        }
        
        Comment savedComment = commentRepository.save(comment);
        return CommentResponse.fromEntity(savedComment);
    }

    /**
     * 按讚留言
     */
    @Transactional
    public CommentResponse likeComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));
        
        comment.setLikes(comment.getLikes() + 1);
        Comment updatedComment = commentRepository.save(comment);
        
        return CommentResponse.fromEntity(updatedComment);
    }

    /**
     * 刪除留言 (邏輯刪除)
     */
    @Transactional
    public void deleteComment(Long commentId, Principal principal) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));
        
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        // 僅允許留言的作者刪除
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new IllegalStateException("You can only delete your own comments");
        }
        
        // 進行邏輯刪除 (設置為非活躍)
        comment.setActive(false);
        commentRepository.save(comment);
    }
}