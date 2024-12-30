package com.crazy.filmzip.service;

import com.crazy.filmzip.dto.AddCommunityPostRequest;
import com.crazy.filmzip.dto.CommunityListViewResponse;
import com.crazy.filmzip.dto.CommunityPostDetailResponse;
import com.crazy.filmzip.dto.UpdateCommunityPostRequest;
import com.crazy.filmzip.entity.CommunityPost;
import com.crazy.filmzip.entity.PostReaction;
import com.crazy.filmzip.entity.User;
import com.crazy.filmzip.repository.CommunityRepository;
import com.crazy.filmzip.repository.PostReactionRepository;
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
    private final PostReactionRepository postReactionRepository;

    // 게시글 목록 조회
    public List<CommunityListViewResponse> findAll() {
        return communityPostRepository.findAll().stream()
                .map(post -> {
                    int likes = postReactionRepository.countByCommunityPostIdAndReactionType(post.getId(), "LIKE");
                    int dislikes = postReactionRepository.countByCommunityPostIdAndReactionType(post.getId(), "DISLIKE");
                    return new CommunityListViewResponse(post, likes, dislikes);
                })
                .toList();
    }

    // 게시글 상세 조회
    @Transactional
    public CommunityPostDetailResponse findById(Long postId) {
        CommunityPost post = communityPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + postId));

        int likes = countReactions(postId, "LIKE");
        int dislikes = countReactions(postId, "DISLIKE");

        return new CommunityPostDetailResponse(post, likes, dislikes);
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

    // 조회수
    @Transactional
    public void incrementViewCount(Long id) {
        CommunityPost post = communityPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + id));
        post.setViews(post.getViews() + 1); // 조회수 증가
    }

    // 추천 비추천 기능
    @Transactional
    public void reactToPost(Long postId, Long userId, String reactionType) {
        CommunityPost post = communityPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + postId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. ID: " + userId));

        // 기존 반응 조회
        PostReaction existingReaction = postReactionRepository.findByUserIdAndCommunityPostId(userId, postId);

        if (existingReaction != null) {
            if (!existingReaction.getReactionType().equals(reactionType)) {
                // 기존 반응이 다른 경우 수정
                existingReaction.setReactionType(reactionType);
                postReactionRepository.save(existingReaction);
            }
        } else {
            // 새로운 반응 추가
            PostReaction newReaction = new PostReaction();
            newReaction.setUser(user); // 유저 객체 설정
            newReaction.setCommunityPost(post);
            newReaction.setReactionType(reactionType);
            postReactionRepository.save(newReaction);
        }
    }

    // 추천 비추천 갯수
    @Transactional
    public int countReactions(Long postId, String reactionType) {
        return postReactionRepository.countByCommunityPostIdAndReactionType(postId, reactionType);
    }

    // 검색 기능
    public List<CommunityListViewResponse> searchPosts(String keyword){

        // 검색 결과 조회
        List<CommunityPost> posts = communityPostRepository.findByTitleContainingOrContentContainingOrUser_NicknameContaining(keyword, keyword, keyword);

        // 검색 결과 DTO 변환
        return posts.stream()
                .map(post -> {
                    int likes = postReactionRepository.countByCommunityPostIdAndReactionType(post.getId(), "LIKE");
                    int dislikes = postReactionRepository.countByCommunityPostIdAndReactionType(post.getId(), "DISLIKE");
                    return new CommunityListViewResponse(post, likes, dislikes);
                })
                .toList();

    }

}