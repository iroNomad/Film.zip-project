/**
 * 장다은
 */
package com.crazy.filmzip.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CommentResponse {
    private Long id;
    private String content;
    private String writer;
    private int likes;
    private int dislikes;
    private int recommends;
    private int notRecommends;
    private List<CommentResponse> replies;

}

