/**
 * 장다은
 */
package com.crazy.filmzip.repository;

import com.crazy.filmzip.entity.Comment;
import com.crazy.filmzip.entity.CommentReaction;
import com.crazy.filmzip.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentReactionRepository extends JpaRepository<CommentReaction, Long> {
    // 특정 댓글에 대한 "LIKE" 반응 수 조회
    long countByCommentIdAndReactionType(Long commentId, String reactionType);
    // 특정 댓글과 사용자에 대한 좋아요 조회
    CommentReaction findByCommentAndUserAndReactionType(Comment comment, User user, String reactionType);
    // 특정 댓글과 사용자에 대한 추천/비추천 조회
    Optional<CommentReaction> findByCommentAndUser(Comment comment, User user);

}
