package com.crazy.filmzip.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequest {
    private String content;

    // 부모 댓글 ID
    private Long parentCommentId;
}