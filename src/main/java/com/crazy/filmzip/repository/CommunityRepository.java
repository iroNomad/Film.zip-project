package com.crazy.filmzip.repository;

import com.crazy.filmzip.entity.CommunityPost;
import com.crazy.filmzip.entity.PostReaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommunityRepository extends JpaRepository<CommunityPost, Long> {

    // ID로 게시글 조회
    Optional<CommunityPost> findById(Long id);

    // 검색 기능(제목 또는 작성자 닉네임)
    Page<CommunityPost> findByTitleContainingOrContentContainingOrUser_NicknameContaining(String titlekeyWord, String writerKeyword, String contentKeyword, Pageable pageable);

    // 닉네임으로 게시글 조회 (작성자가 특정 닉네임인 게시글 목록)
    List<CommunityPost> findAllByUser_Nickname(String nickname);
}
