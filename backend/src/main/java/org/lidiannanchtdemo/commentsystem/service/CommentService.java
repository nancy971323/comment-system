package org.lidiannanchtdemo.commentsystem.service;

import org.lidiannanchtdemo.commentsystem.dto.CommentDTO;
import org.lidiannanchtdemo.commentsystem.model.Comment;
import org.lidiannanchtdemo.commentsystem.model.CommentLike;
import org.lidiannanchtdemo.commentsystem.model.User;
import org.lidiannanchtdemo.commentsystem.repository.CommentRepository;
import org.lidiannanchtdemo.commentsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 留言服務
 * 處理留言的創建、獲取、更新和刪除
 */
@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    
    @Autowired
    private UserRepository userRepository;

    /**
     * 獲取所有頂層留言及其回覆
     * @param currentUserId 當前用戶ID，用於確定用戶是否點讚
     * @return 留言列表
     */
    public List<CommentDTO> getAllComments(Long currentUserId) {
        // 獲取所有頂層留言（沒有父留言的留言）
        List<Comment> topLevelComments = commentRepository.findByParentIsNullOrderByCreatedAtDesc();
        
        // 將留言轉換為DTO並返回
        return topLevelComments.stream()
                .map(comment -> CommentDTO.fromEntity(comment, currentUserId))
                .collect(Collectors.toList());
    }

    /**
     * 創建新留言或回覆
     * @param content 留言內容
     * @param authorId 作者ID
     * @param parentId 父留言ID，如果是頂層留言則為null
     * @return 創建的留言DTO
     */
    @Transactional
    public CommentDTO createComment(String content, Long authorId, Long parentId) {
        // 查找作者
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("用戶不存在"));
        
        // 創建新留言
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setAuthor(author);
        
        // 如果是回覆，設置父留言
        if (parentId != null) {
            Comment parentComment = commentRepository.findById(parentId)
                    .orElseThrow(() -> new RuntimeException("父留言不存在"));
            comment.setParent(parentComment);
        }
        
        // 保存留言
        Comment savedComment = commentRepository.save(comment);
        
        // 返回留言DTO
        return CommentDTO.fromEntity(savedComment, authorId);
    }

    /**
     * 給留言點讚
     * @param commentId 留言ID
     * @param userId 用戶ID
     */
    @Transactional
    public void likeComment(Long commentId, Long userId) {
        // 查找留言和用戶
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("留言不存在"));
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用戶不存在"));
        
        // 檢查用戶是否已經點讚
        boolean alreadyLiked = comment.isLikedBy(user);
        
        if (!alreadyLiked) {
            // 如果還沒點讚，添加點讚
            comment.addLike(user);
            commentRepository.save(comment);
        }
    }

    /**
     * 刪除留言
     * @param commentId 留言ID
     * @param userId 當前用戶ID
     * @return 是否成功刪除
     */
    @Transactional
    public boolean deleteComment(Long commentId, Long userId) {
        // 查找留言
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        
        if (commentOptional.isPresent()) {
            Comment comment = commentOptional.get();
            
            // 檢查當前用戶是否是留言作者
            if (comment.getAuthor().getId().equals(userId)) {
                // 如果是作者，刪除留言
                commentRepository.delete(comment);
                return true;
            }
        }
        
        // 留言不存在或當前用戶不是作者
        return false;
    }
}