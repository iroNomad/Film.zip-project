package com.crazy.filmzip.dto;

import com.crazy.filmzip.entity.CommunityPost;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class CommunityListViewResponse {
    // 데이터를 반환 (응답용)
    private Long id;
    private String title;
    private String content;
    private String writer;
    private int views;

    public CommunityListViewResponse(CommunityPost post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.writer = post.getUser().getNickname();
        this.views = post.getViews();
    }
}
