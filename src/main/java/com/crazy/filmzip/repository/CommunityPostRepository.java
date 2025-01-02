package com.crazy.filmzip.repository;

import com.crazy.filmzip.entity.CommunityPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityPostRepository extends JpaRepository<CommunityPost, Long> {
    // 필요한 커스텀 메소드는 여기에 추가
}
