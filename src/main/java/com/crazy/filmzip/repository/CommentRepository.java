package com.crazy.filmzip.repository;

import com.crazy.filmzip.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByCommunityPostId(Long postId);
    List<Comment> findByCommunityPostIdAndParentCommentIdIsNull(Long postId);
    List<Comment> findByParentCommentId(Long parentCommentId);
}