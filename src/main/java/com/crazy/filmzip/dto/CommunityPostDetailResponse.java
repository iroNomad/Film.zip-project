package com.crazy.filmzip.dto;

import com.crazy.filmzip.entity.CommunityPost;
import com.crazy.filmzip.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CommunityPostDetailResponse {
    private Long id;         // 게시글 ID
    private String title;    // 게시글 제목
    private String content;  // 게시글 내용
    private String writer;   // 작성자 닉네임
    private int views;       // 조회수
    private String createdAt;// 작성일
    private String updatedAt;// 수정일
    private int likes;       // 추천 수
    private int dislikes;    // 비추천 수

    public CommunityPostDetailResponse(CommunityPost post, int likes, int dislikes) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.writer = post.getUser().getNickname(); // 작성자 닉네임
        this.views = post.getViews();
        this.createdAt = post.getCreatedAt().toString();
        this.updatedAt = post.getUpdatedAt().toString();
        this.likes = likes;
        this.dislikes = dislikes;
    }

}
