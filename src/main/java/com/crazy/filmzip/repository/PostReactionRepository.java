/**
 * 이지민
 */
package com.crazy.filmzip.repository;

import com.crazy.filmzip.entity.PostReaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostReactionRepository extends JpaRepository<PostReaction, Long> {
    // 특정 사용자의 특정 게시글에 대한 반응 조회
    PostReaction findByUserIdAndCommunityPostId(Long userId, Long postId);

    // 특정 게시글의 반응 수 계산
    int countByCommunityPostIdAndReactionType(Long postId, String reactionType);
}
