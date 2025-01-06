/**
 * 장다은
 */
package com.crazy.filmzip.repository;

import com.crazy.filmzip.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 게시글 최상위 댓글 조회
    List<Comment> findByCommunityPostIdAndParentCommentIsNull(Long postId);

    // 특정 댓글 대댓글 조회
    List<Comment> findByParentCommentId(Long parentId);


    List<Comment> findByCommunityPostIdAndParentCommentIdIsNull(Long postId);
}
