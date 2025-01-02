package com.crazy.filmzip.service;

import com.crazy.filmzip.entity.Comment;
import com.crazy.filmzip.entity.CommentReaction;
import com.crazy.filmzip.entity.User;
import com.crazy.filmzip.repository.CommentReactionRepository;
import com.crazy.filmzip.repository.CommentRepository;
import com.crazy.filmzip.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentReactionService {
    @Autowired
    private CommentReactionRepository commentReactionRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    // 좋아요 토글 처리 (추가 또는 삭제)
    public int toggleLike(Long commentId, Long userId) {
        int rtn = -1;
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

        // 기존에 좋아요를 눌렀는지 확인
        CommentReaction existingReaction = commentReactionRepository.findByCommentAndUserAndReactionType(comment, user, "LIKE");

        if (existingReaction != null) {
            // 이미 좋아요가 눌러졌으면 삭제
            rtn = 0;
            commentReactionRepository.delete(existingReaction);
        } else {
            // 좋아요 추가
            CommentReaction newReaction = new CommentReaction();
            newReaction.setComment(comment);
            newReaction.setUser(user);
            newReaction.setReactionType("LIKE"); // 좋아요 설정
            rtn = 1;
            commentReactionRepository.save(newReaction);
        }

        return rtn;
    }
}

