/**
 * 이지민
 */
package com.crazy.filmzip.dto;

import com.crazy.filmzip.entity.CommunityPost;
import lombok.Getter;

@Getter
public class CommunityPostResponse {
    private Long id;
    private String title;
    private String content;
    private String writer;
    private int views;

    public CommunityPostResponse(CommunityPost post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.writer = post.getUser().getNickname();
        this.views = post.getViews();
    }
}