
package com.crazy.filmzip.controller;

import com.crazy.filmzip.dto.AddCommunityPostRequest;
import com.crazy.filmzip.dto.AddReplyRequest;
import com.crazy.filmzip.dto.CommentRequestDto;
import com.crazy.filmzip.dto.CommunityPostDetailResponse;
import com.crazy.filmzip.entity.Comment;
import com.crazy.filmzip.entity.CommunityPost;
import com.crazy.filmzip.entity.User;
import com.crazy.filmzip.repository.CommentReactionRepository;
import com.crazy.filmzip.repository.UserRepository;
import com.crazy.filmzip.service.CommentService;
import com.crazy.filmzip.service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
}
