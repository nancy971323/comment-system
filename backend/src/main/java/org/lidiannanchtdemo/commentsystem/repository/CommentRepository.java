package org.lidiannanchtdemo.commentsystem.repository;

import org.lidiannanchtdemo.commentsystem.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 留言數據訪問接口
 * 提供留言CRUD操作和自定義查詢方法
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    /**
     * 查詢所有頂層留言（沒有父留言的留言）
     * 按創建時間降序排序
     */
    List<Comment> findByParentIsNullOrderByCreatedAtDesc();
    
    /**
     * 查詢特定用戶創建的所有留言
     * 按創建時間降序排序
     */
    List<Comment> findByAuthorIdOrderByCreatedAtDesc(Long authorId);
    
    /**
     * 查詢特定留言的所有回覆
     * 按創建時間升序排序
     */
    List<Comment> findByParentIdOrderByCreatedAtAsc(Long parentId);
    
    /**
     * 查詢特定留言被點讚的次數
     */
    @Query("SELECT COUNT(cl) FROM CommentLike cl WHERE cl.comment.id = :commentId")
    int countLikesByCommentId(Long commentId);
}