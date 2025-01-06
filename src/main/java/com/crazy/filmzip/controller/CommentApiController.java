/**
 * 장다은
 */
package com.crazy.filmzip.controller;

import com.crazy.filmzip.config.jwt.TokenProvider;
import com.crazy.filmzip.dto.AddCommunityPostRequest;
import com.crazy.filmzip.dto.ReactionRequest;
import com.crazy.filmzip.entity.Comment;
import com.crazy.filmzip.entity.CommunityPost;
import com.crazy.filmzip.entity.User;
import com.crazy.filmzip.repository.CommentReactionRepository;
import com.crazy.filmzip.repository.UserRepository;
import com.crazy.filmzip.service.CommentReactionService;
import com.crazy.filmzip.service.CommentService;
import com.crazy.filmzip.service.CommunityService;
import com.crazy.filmzip.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RestController
@RequestMapping("/api/comments")

public class CommentApiController {

    private final CommentService commentService;
    private final UserRepository userRepository;
    private final CommentReactionRepository commentReactionRepository;
    private final CommentReactionService commentReactionService;
    private final CommunityService communityService;
    private final UserService userService;
    private final TokenProvider tokenProvider;

    // 현재 로그인한 사용자 정보 얻기
    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. 사용자명: " + username));
    }

    @GetMapping("/user/current")
    public ResponseEntity<Map<String, Object>> getCurrentUser(Principal principal) {

        // 현재 로그인한 사용자 정보 얻기
        User user = getAuthenticatedUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("userId", user.getId());
        response.put("userNickName", user.getNickname());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create/{postId}")
    public ResponseEntity<String> createComment(
            @PathVariable("postId") Long postId,
            @RequestBody Map<String, Object> payload) {

        // 요청에서 content와 parentCommentId 가져오기
        String content = (String) payload.get("content");
        Long parentCommentId = payload.containsKey("parentCommentId") ?
                ((Number) payload.get("parentCommentId")).longValue() : null;

        // 게시글 확인
        CommunityPost post = communityService.findPostById(postId);
        if (post == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("게시글을 찾을 수 없습니다. ID: " + postId);
        }

        // 현재 로그인한 사용자 정보 얻기
        User user = getAuthenticatedUser();

        // 댓글 생성 요청 객체 설정
        AddCommunityPostRequest request = new AddCommunityPostRequest();
        request.setUserId(user.getId());

        // 댓글 생성 처리
        commentService.create(post, content, request, parentCommentId);

        // 성공 응답 반환
        return ResponseEntity.ok("댓글이 성공적으로 등록되었습니다.");
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<String> editComment(
            @PathVariable("id") Long id,
            @RequestBody Map<String, String> requestData) {

        // 현재 로그인한 사용자 정보 얻기
        User user = getAuthenticatedUser();

        // 댓글 가져오기
        Comment comment = commentService.getComment(id);

        // 댓글 작성자와 현재 사용자 비교
        if (!comment.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
        }

        // 수정할 내용 가져오기
        String content = requestData.get("content");
        if (content == null || content.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("내용을 입력하세요.");
        }

        // 댓글 수정
        commentService.modify(comment, content);
        return ResponseEntity.ok("댓글이 수정되었습니다.");
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable("id") Long id) {
        // 현재 로그인한 사용자 정보 얻기
        User user = getAuthenticatedUser();
        // 댓글 가져오기
        Comment comment = commentService.getComment(id);
        // 댓글 작성자와 현재 사용자 비교
        if (!comment.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
        }

        commentService.delete(comment);
        return ResponseEntity.ok("댓글이 삭제되었습니다.");
    }

    @PostMapping("/like/{commentId}")
    public ResponseEntity<Map<String, Object>> toggleLike(@PathVariable Long commentId) {
        User user = getAuthenticatedUser();  // 현재 로그인한 유저 정보 가져오기

        int rtn = commentReactionService.toggleLike(commentId, user.getId());
        long likeCount = commentReactionRepository.countByCommentIdAndReactionType(commentId, "LIKE");

        Map<String, Object> response = new HashMap<>();
        response.put("message", rtn == 0 ? "좋아요가 취소되었습니다." : "좋아요가 등록되었습니다.");
        response.put("likeCount", likeCount);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{commentId}/recommend")
    public ResponseEntity<Map<String, Integer>> recommendComment(@PathVariable Long commentId) {
        User user = getAuthenticatedUser();  // 현재 로그인한 유저 정보 가져오기
        int updatedRecommends = commentService.recommendComment(commentId, user.getId());
        Map<String, Integer> response = Map.of("recommends", updatedRecommends);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{commentId}/notRecommend")
    public ResponseEntity<Map<String, Integer>> notRecommendComment(@PathVariable Long commentId) {
        User user = getAuthenticatedUser();  // 현재 로그인한 유저 정보 가져오기
        int updatedNotRecommends = commentService.notRecommendComment(commentId, user.getId());
        Map<String, Integer> response = Map.of("notRecommends", updatedNotRecommends);
        return ResponseEntity.ok(response);
    }
/*
    @PostMapping("/reaction")
    public ResponseEntity<Map<String, String>> handleReaction(@RequestBody ReactionRequest request) {
        Map<String, String> response = new HashMap<>();

        // 현재 로그인한 사용자 정보 얻기
        User user = getAuthenticatedUser();
        if (user == null) {
            response.put("status", "ERROR");
            response.put("message", "사용자 인증이 필요합니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        // 댓글 조회
        Comment comment = commentService.findById(request.getCommentId());
        if (comment == null) {
            response.put("status", "ERROR");
            response.put("message", "댓글을 찾을 수 없습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // 반응 처리 로직
        if ("RECOMMEND".equalsIgnoreCase(request.getReaction())) {
            if (comment.getNotRecommends() > 0) {
                comment.setNotRecommends(comment.getNotRecommends() - 1);
            }
            comment.setRecommends(comment.getRecommends() + 1);
        } else if ("NOTRECOMMEND".equalsIgnoreCase(request.getReaction())) {
            if (comment.getRecommends() > 0) {
                comment.setRecommends(comment.getRecommends() - 1);
            }
            comment.setNotRecommends(comment.getNotRecommends() + 1);
        } else {
            response.put("status", "ERROR");
            response.put("message", "잘못된 반응 요청입니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // 변경사항 저장
        commentService.save(comment);

        response.put("status", "OK");
        return ResponseEntity.ok(response);
    }
*/
    // 댓글 추천/비추천 리셋 업데이트
    @PostMapping("/reaction")
    @ResponseBody
    public ResponseEntity<?> updateReaction(@RequestBody ReactionRequest request) {
        // 현재 로그인한 사용자 정보 얻기
        User user = getAuthenticatedUser();

        Map<String, String> response = new HashMap<>();
        if(commentService.handleReaction(request.getCommentId(), user.getId(), request)) {
            Comment comment = commentService.findById(request.getCommentId());
            if (request.getReaction().equals("RECOMMEND")) {
                if (comment.getNotRecommends() > 0) {
                    comment.setNotRecommends(comment.getNotRecommends() - 1);
                }
                comment.setRecommends(comment.getRecommends() + 1);
            } else if (request.getReaction().equals("NOTRECOMMEND")) {
                if (comment.getRecommends() > 0) {
                    comment.setRecommends(comment.getRecommends() - 1);
                }
                comment.setNotRecommends(comment.getNotRecommends() + 1);
            }
            commentService.save(comment);
            response.put("status", "OK");
        } else {
            response.put("status", "NOT_OK");
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/reply/{postId}/{parentCommentId}")
    public ResponseEntity<String> createReply(
            @PathVariable Long postId,
            @PathVariable Long parentCommentId,
            @RequestBody Map<String, Object> requestData) {

        // 요청에서 content 가져오기
        String content = (String) requestData.get("content");

        // 게시글 확인
        CommunityPost post = communityService.findPostById(postId);
        if (post == null) {
            throw new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + postId);
        }

        // 인증된 사용자 정보 가져오기
        User user = getAuthenticatedUser();

        AddCommunityPostRequest request = new AddCommunityPostRequest();
        request.setUserId(user.getId());

        commentService.create(post, content, request, parentCommentId);

        // 성공 응답 반환
        return ResponseEntity.ok("답글이 성공적으로 등록되었습니다.");
    }

}
