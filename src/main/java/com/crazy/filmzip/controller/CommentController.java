
package com.crazy.filmzip.controller;

import com.crazy.filmzip.dto.AddCommunityPostRequest;
import com.crazy.filmzip.dto.AddReplyRequest;
import com.crazy.filmzip.dto.CommentRequestDto;

import com.crazy.filmzip.dto.ReactionRequest;
import com.crazy.filmzip.entity.Comment;
import com.crazy.filmzip.entity.CommunityPost;
import com.crazy.filmzip.entity.User;
import com.crazy.filmzip.repository.CommentReactionRepository;
import com.crazy.filmzip.repository.UserRepository;

import com.crazy.filmzip.service.CommentReactionService;
import com.crazy.filmzip.service.CommentService;
import com.crazy.filmzip.service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/comments")
@RequiredArgsConstructor
//@RestController
@Controller
public class CommentController {

    private final CommentService commentService;
    private final CommunityService communityService;
    private final UserRepository userRepository;
    private final CommentReactionRepository commentReactionRepository;

    // 댓글 생성
    @PostMapping("/create/{postId}")
    public String createComment(
            @PathVariable("postId") Long postId,
            @RequestParam("content") String content,
            @RequestParam(value = "parentCommentId", required = false) Long parentCommentId,
            Principal principal) {

        // 게시글 가져오기
        CommunityPost post = communityService.findPostById(postId);

        if (post == null) {
            throw new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: ");
        }

        // AddCommunityPostRequest에 사용자 정보 넣기
        AddCommunityPostRequest request = new AddCommunityPostRequest();
        request.setUserId(5L);

        // 댓글 저장
        commentService.create(post, content, request, parentCommentId);

        // 상세 페이지로 리다이렉트
        return String.format("redirect:/community/%s", postId);
    }


    // 댓글 수정
    @PutMapping("/edit/{id}")
    @ResponseBody
    public String editComment(
            @PathVariable("id") Long id,
            @RequestBody Map<String, String> requestData,  // JSON 데이터 구조 받기
            Principal principal) {
        Comment comment = commentService.getComment(id);
        String content = requestData.get("content");  // content 값 받기

        //if (!comment.getUser().getEmail().equals(principal.getName())) {
        //    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "수정 권한이 없습니다.");
        //}

        commentService.modify(comment, content);
        return "댓글이 수정되었습니다.";
    }


    // 댓글 삭제
    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public String deleteComment(
            @PathVariable("id") Long id,
            Principal principal) {
        Comment comment = commentService.getComment(id);

        //if (!comment.getUser().getEmail().equals(principal.getName())) {
        //    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "삭제 권한이 없습니다.");
        //}

        commentService.delete(comment);
        return "댓글이 삭제되었습니다.";
    }

    @Autowired
    private CommentReactionService commentReactionService;

    @PostMapping("/like/{commentId}")
    public ResponseEntity<Map<String, Object>> toggleLike(@PathVariable Long commentId, Principal principal) {
        AddCommunityPostRequest request = new AddCommunityPostRequest();
        request.setUserId(2L);
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. ID: " + request.getUserId()));

        int rtn = commentReactionService.toggleLike(commentId, user.getId());
        long likeCount = commentReactionRepository.countByCommentIdAndReactionType(commentId, "LIKE"); // 좋아요 수 계산

        Map<String, Object> response = new HashMap<>();
        if(rtn == 0) response.put("message", "좋아요가 취소되었습니다.");
        else response.put("message", "좋아요가 등록되었습니다.");
        response.put("likeCount", likeCount);

        return ResponseEntity.ok(response);
    }

    // 대댓글 생성
    @PostMapping("/reply/{postId}/{parentCommentId}")
    public String createReply(@PathVariable Long postId, @PathVariable Long parentCommentId,
                              @RequestParam String content) {

        // 댓글 저장 (대댓글도 동일하게 처리됨)
        AddCommunityPostRequest request = new AddCommunityPostRequest();

        request.setUserId(5L);


        CommunityPost post = communityService.findPostById(postId);

        if (post == null) {
            throw new IllegalArgumentException("게시글을 찾을 수 없습니다.");
        }

        // 대댓글 생성
        commentService.create(post, content, request, parentCommentId);

        // 게시글 상세 페이지로 리디렉션
        return "redirect:/community/" + postId;
    }

    // 댓글 추천
    @PostMapping("/{commentId}/recommend")
    public ResponseEntity<Map<String, Integer>> recommendComment(@PathVariable Long commentId) {
        int updatedRecommends = commentService.recommendComment(commentId);
        Map<String, Integer> response = Map.of("recommends", updatedRecommends);
        return ResponseEntity.ok(response);
    }

    // 댓글 비추천
    @PostMapping("/{commentId}/notRecommend")
    public ResponseEntity<Map<String, Integer>> notRecommendComment(@PathVariable Long commentId) {
        int updatedNotRecommends = commentService.notRecommendComment(commentId);
        Map<String, Integer> response = Map.of("notRecommends", updatedNotRecommends);
        return ResponseEntity.ok(response);
    }

    // 댓글 추천/비추천 리셋 업데이트
    @PostMapping("/reaction")
    @ResponseBody
    public ResponseEntity<?> updateReaction(@RequestBody ReactionRequest request) {
        Map<String, String> response = new HashMap<>();
        if(commentService.handleReaction(request.getCommentId(), 2L, request)) {
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

}
