package com.crazy.filmzip.service;

import com.crazy.filmzip.dto.AddCommunityPostRequest;
import com.crazy.filmzip.dto.CommentRequest;
import com.crazy.filmzip.dto.CommentResponse;
import com.crazy.filmzip.entity.Comment;
import com.crazy.filmzip.entity.CommunityPost;
import com.crazy.filmzip.entity.User;
import com.crazy.filmzip.repository.CommentRepository;

import com.crazy.filmzip.repository.CommunityRepository;
import com.crazy.filmzip.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    private final CommunityRepository communityRepository;

    public Comment create(CommunityPost post, String content, AddCommunityPostRequest request, Long parentCommentId) {
        // 사용자를 찾기
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. ID: " + request.getUserId()));

        // 부모 댓글 ID가 있는 경우, 대댓글 처리
        if (parentCommentId != null) {
            // 부모 댓글을 찾아 대댓글로 설정
            Comment parentComment = commentRepository.findById(parentCommentId)
                    .orElseThrow(() -> new IllegalArgumentException("부모 댓글을 찾을 수 없습니다."));

            // 대댓글 생성
            Comment reply = new Comment();
            reply.setContent(content);
            reply.setCommunityPost(post);  // 부모 댓글이 속한 게시글 설정
            // 대댓글은 부모 댓글의 replies 목록에만 추가
            parentComment.getReplies().add(reply);
            reply.setUser(user);
            reply.setParentComment(parentComment); // 부모 댓글 설정
            this.commentRepository.save(reply); // 대댓글 저장
            return reply;
        } else {
            // 부모 댓글 ID가 없으면 일반 댓글로 저장
            Comment comment = new Comment();
            comment.setContent(content);
            comment.setCommunityPost(post);  // 게시글 설정
            comment.setUser(user);
            this.commentRepository.save(comment); // 일반 댓글 저장
            return comment;
        }
    }


    // 댓글 수정
    public void modify(Comment comment, String content) {
        comment.setContent(content);
        this.commentRepository.save(comment);
    }

    // 댓글 삭제
    public void delete(Comment comment) {
        this.commentRepository.delete(comment);
    }

    // 댓글 조회 (ID로 찾기)
    public Comment getComment(Long id) {
        Optional<Comment> comment = this.commentRepository.findById(id);
        return comment.orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));
    }

    // 부모 댓글 찾기
    public Comment findById(Long parentCommentId) {
        return commentRepository.findById(parentCommentId)
                .orElseThrow(() -> new IllegalArgumentException("부모 댓글을 찾을 수 없습니다. ID: " + parentCommentId));
    }

    // 특정 게시글에 달린 댓글들 조회
    public List<Comment> findByPostId(Long postId) {
        //return commentRepository.findByCommunityPostId(postId);

        return commentRepository.findByCommunityPostIdAndParentCommentIdIsNull(postId);
    }
    // CommunityPost를 ID로 조회하는 메소드 추가
    public CommunityPost findPostById(Long postId) {
        return communityRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + postId));
    }
}

