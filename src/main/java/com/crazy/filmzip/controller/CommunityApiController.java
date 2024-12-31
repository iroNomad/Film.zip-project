package com.crazy.filmzip.controller;

import com.crazy.filmzip.dto.*;
import com.crazy.filmzip.entity.CommunityPost;
import com.crazy.filmzip.service.CommentService;
import com.crazy.filmzip.service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/community")
public class CommunityApiController {

    private final CommunityService communityService;

    @Autowired
    private CommentService commentService;

    // 게시글 목록 조회
    @GetMapping
    public ResponseEntity<List<CommunityListViewResponse>> getAllPosts() {
        List<CommunityListViewResponse> posts = communityService.findAll();
        return ResponseEntity.ok(posts);
    }

    // 게시글 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<CommunityPostDetailResponse> getPostById(@PathVariable Long id) {

        CommunityPost post = communityService.findById(id);
        return ResponseEntity.ok(new CommunityPostDetailResponse(post));
    }

    // 게시글 생성
    @PostMapping
    public ResponseEntity<CommunityPostResponse> addPost(@RequestBody AddCommunityPostRequest request) {
        // 기본 테스트 사용자 ID를 설정
        request.setUserId(7L);

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
}