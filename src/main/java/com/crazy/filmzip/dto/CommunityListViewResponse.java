/**
 * 이지민
 */
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
    private int likes;
    private int dislikes;


    public CommunityListViewResponse(CommunityPost post, int likes, int dislikes) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.writer = post.getUser().getNickname();
        this.views = post.getViews();
        this.likes = likes;
        this.dislikes = dislikes;
    }
}
