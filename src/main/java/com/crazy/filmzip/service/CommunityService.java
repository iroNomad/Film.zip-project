package com.crazy.filmzip.service;

import com.crazy.filmzip.dto.AddCommunityPostRequest;
import com.crazy.filmzip.dto.CommunityListViewResponse;
import com.crazy.filmzip.dto.UpdateCommunityPostRequest;
import com.crazy.filmzip.entity.CommunityPost;
import com.crazy.filmzip.entity.User;
import com.crazy.filmzip.repository.CommunityRepository;
import com.crazy.filmzip.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommunityService {

    private final CommunityRepository communityPostRepository;
    private final UserRepository userRepository;

    // 게시글 목록 조회
    public List<CommunityListViewResponse> findAll() {
        return communityPostRepository.findAll().stream()
                .map(CommunityListViewResponse::new)
                .toList();
    }

    // 게시글 상세 조회
    public CommunityPost findById(Long id) {
        return communityPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + id));
    }

    // 게시글 생성
    public CommunityPost save(AddCommunityPostRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. ID: " + request.getUserId()));

        return communityPostRepository.save(request.toEntity(user));
    }

    // 게시글 수정
    @Transactional
    public CommunityPost update(Long id, UpdateCommunityPostRequest request) {
        CommunityPost post = communityPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + id));

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        return communityPostRepository.save(post);
    }

    // 게시글 삭제
    public void delete(Long id) {
        if (!communityPostRepository.existsById(id)) {
            throw new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + id);
        }
        communityPostRepository.deleteById(id);
    }

    @Transactional
    public void incrementViewCount(Long id) {
        CommunityPost post = communityPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + id));
        post.setViews(post.getViews() + 1); // 조회수 증가
    }

}