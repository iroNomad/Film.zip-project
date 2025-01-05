package com.crazy.filmzip.controller;

import com.crazy.filmzip.dto.AddCommunityPostRequest;
import com.crazy.filmzip.entity.*;
import com.crazy.filmzip.repository.UserRepository;
import com.crazy.filmzip.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/comments")
@RequiredArgsConstructor
@Controller
public class CommentController {

    private final CommentService commentService;
    private final CommunityService communityService;
    private final UserRepository userRepository;

    /*
    @PostMapping("/create/{postId}")
    public String createComment(
            @PathVariable("postId") Long postId,
            @RequestParam("content") String content,
            @RequestParam(value = "parentCommentId", required = false) Long parentCommentId) {

        CommunityPost post = communityService.findPostById(postId);
        if (post == null) {
            throw new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + postId);
        }

        // 인증된 사용자 정보 가져오기
        //User user = getAuthenticatedUser();
        User user = getCurrentUser(); // 현재 인증된 사용자 가져오기

        AddCommunityPostRequest request = new AddCommunityPostRequest();
        request.setUserId(user.getId());

        commentService.create(post, content, request, parentCommentId);
        return String.format("redirect:/community/%s", postId);
    }
    */
    /*
    @PostMapping("/reply/{postId}/{parentCommentId}")
    public String createReply(
            @PathVariable Long postId,
            @PathVariable Long parentCommentId,
            @RequestParam String content) {

        CommunityPost post = communityService.findPostById(postId);
        if (post == null) {
            throw new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + postId);
        }

        // 인증된 사용자 정보 가져오기
        User user = getAuthenticatedUser();

        AddCommunityPostRequest request = new AddCommunityPostRequest();
        request.setUserId(user.getId());

        commentService.create(post, content, request, parentCommentId);
        return String.format("redirect:/community/%s", postId);
    }
    */

}
