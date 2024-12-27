package com.crazy.filmzip.repository;

import com.crazy.filmzip.entity.CommunityPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommunityRepository extends JpaRepository<CommunityPost, Long> {

    // ID로 게시글 조회
    Optional<CommunityPost> findById(Long id);

    // 닉네임으로 게시글 조회 (작성자가 특정 닉네임인 게시글 목록)
    List<CommunityPost> findAllByUser_Nickname(String nickname);
}
