package com.crazy.filmzip.controller;

import com.crazy.filmzip.dto.*;
import com.crazy.filmzip.entity.CommunityPost;
import com.crazy.filmzip.entity.User;
import com.crazy.filmzip.service.CommentService;
import com.crazy.filmzip.service.CommunityService;
import com.crazy.filmzip.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/community")
public class CommunityApiController {

    private final CommunityService communityService;
    private final UserService userService;

    // 현재 인증된 사용자 가져오기
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authentication: " + authentication);

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("현재 사용자 인증 정보가 없습니다.");
        }

        String email = authentication.getName();
        System.out.println("Authenticated User Email: " + email);

        return userService.findByEmail(email);
    }

//    private CommentService commentService;

    // 게시글 목록 조회
    @GetMapping
    public ResponseEntity<Page<CommunityListViewResponse>> getAllPosts(Pageable pageable) {
        Page<CommunityListViewResponse> posts = communityService.findAll(pageable);
        return ResponseEntity.ok(posts);
    }

    // 게시글 상세 조회 (추천/비추천 포함)
    @GetMapping("/{id}")
    public ResponseEntity<CommunityPostDetailResponse> getPostById(@PathVariable Long id) {

        User currentUser = getCurrentUser();

        CommunityPostDetailResponse postDetail = communityService.findById(id);
        return ResponseEntity.ok(postDetail);
    }

    // 게시글 생성
    @PostMapping
    public ResponseEntity<CommunityPostResponse> addPost(@RequestBody AddCommunityPostRequest request) {
        try {
            User currentUser = getCurrentUser(); // 현재 인증된 사용자 가져오기
            System.out.println("Current User: " + currentUser);

            request.setUserId(currentUser.getId()); // 사용자 ID 설정
            System.out.println("Request: " + request);

            CommunityPost savedPost = communityService.save(request);
            System.out.println("Saved Post: " + savedPost);

            return ResponseEntity.status(HttpStatus.CREATED).body(new CommunityPostResponse(savedPost));
        } catch (Exception e) {
            e.printStackTrace(); // 예외 출력
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 에러 반환
        }
    }


    // 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<CommunityPostResponse> updatePost(
            @PathVariable Long id,
            @RequestBody UpdateCommunityPostRequest request) {

        User currentUser = getCurrentUser();

        // 게시글 작성자 검증
        CommunityPostDetailResponse existingPost = communityService.findById(id);
        if (!existingPost.getWriter().equals(currentUser.getNickname())) {
            throw new AccessDeniedException("You do not have permission to update this post");
        }

        CommunityPost updatedPost = communityService.update(id, request);
        return ResponseEntity.ok(new CommunityPostResponse(updatedPost));
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        User currentUser = getCurrentUser();

        // 게시글 작성자 검증
        CommunityPostDetailResponse existingPost = communityService.findById(id);
        if (!existingPost.getWriter().equals(currentUser.getNickname())) {
            throw new AccessDeniedException("You do not have permission to delete this post");
        }

        communityService.delete(id);
        return ResponseEntity.ok().build();
    }

    // 게시글 추천/비추천
    @PostMapping("/{id}/react")
    public ResponseEntity<Void> reactToPost(@PathVariable Long id,
                                            @RequestParam String reactionType) {
        User currentUser = getCurrentUser(); // 현재 인증된 사용자 가져오기

        communityService.reactToPost(id, currentUser.getId(), reactionType);
        return ResponseEntity.ok().build();
    }

    // 게시글의 추천/비추천 수 조회
    @GetMapping("/{id}/reactions")
    public ResponseEntity<Map<String, Integer>> getReactions(@PathVariable Long id) {
        int likes = communityService.countReactions(id, "LIKE");
        int dislikes = communityService.countReactions(id, "DISLIKE");

        Map<String, Integer> response = new HashMap<>();
        response.put("likes", likes);
        response.put("dislikes", dislikes);
        return ResponseEntity.ok(response);
    }
}
