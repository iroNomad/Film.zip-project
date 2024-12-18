package com.crazy.filmzip.dto;

import com.crazy.filmzip.entity.CommunityPost;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class CommunityListViewResponse {
    private Long id;
    private String title;
    private String content;
    private int views; // 조회수
    private String writer;
    private LocalDateTime createdAt; // 작성 시간

    public CommunityListViewResponse(CommunityPost communityPost) {
        this.id = communityPost.getId();
        this.title = communityPost.getTitle();
        this.content = communityPost.getContent();
        this.views = communityPost.getViews();
        this.writer = communityPost.getUser().getNickname();
        this.createdAt = communityPost.getCreatedAt();
    }
}
