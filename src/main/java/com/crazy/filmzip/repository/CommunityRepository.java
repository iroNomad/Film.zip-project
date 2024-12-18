package com.crazy.filmzip.repository;

import com.crazy.filmzip.entity.CommunityPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityRepository extends JpaRepository<CommunityPost, Long> {
}
