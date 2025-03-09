package org.lidiannan.demo.repository;

import org.lidiannan.demo.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    // 查詢所有頂層留言（非回覆）
    @Query("SELECT c FROM Comment c WHERE c.parentComment IS NULL AND c.active = true ORDER BY c.createdAt DESC")
    List<Comment> findAllMainComments();
    
    // 查詢特定留言的所有回覆
    List<Comment> findByParentCommentIdAndActiveOrderByCreatedAtAsc(Long parentId, Boolean active);
}