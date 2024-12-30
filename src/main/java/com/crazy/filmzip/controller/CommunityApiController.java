package com.crazy.filmzip.controller;

import com.crazy.filmzip.dto.*;
import com.crazy.filmzip.entity.CommunityPost;
import com.crazy.filmzip.service.CommunityService;
import lombok.RequiredArgsConstructor;
import oracle.jdbc.proxy.annotation.Post;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/community")
public class CommunityApiController {

    private final CommunityService communityService;

    // 게시글 목록 조회
    @GetMapping
    public ResponseEntity<List<CommunityListViewResponse>> getAllPosts() {
        List<CommunityListViewResponse> posts = communityService.findAll();
        return ResponseEntity.ok(posts);
    }

    // 게시글 상세 조회 (추천/비추천 포함)
    @GetMapping("/{id}")
    public ResponseEntity<CommunityPostDetailResponse> getPostById(@PathVariable Long id) {
        CommunityPostDetailResponse postDetail = communityService.findById(id); // 수정된 메서드 호출
        return ResponseEntity.ok(postDetail);
    }

    // 게시글 생성
    @PostMapping
    public ResponseEntity<CommunityPostResponse> addPost(@RequestBody AddCommunityPostRequest request) {
        // 기본 테스트 사용자 ID를 설정
        request.setUserId(26L);

        CommunityPost savedPost = communityService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommunityPostResponse(savedPost));
    }

    // 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<CommunityPostResponse> updatePost(
            @PathVariable Long id,
            @RequestBody UpdateCommunityPostRequest request) {
        CommunityPost updatedPost = communityService.update(id, request);
        return ResponseEntity.ok(new CommunityPostResponse(updatedPost));
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        communityService.delete(id);
        return ResponseEntity.ok().build();
    }

    // 게시글 추천/비추천
    @PostMapping("/{id}/react")
    public ResponseEntity<Void> reactToPost(@PathVariable Long id,
                                            @RequestParam Long userId,
                                            @RequestParam String reactionType) {
        communityService.reactToPost(id, userId, reactionType);
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
