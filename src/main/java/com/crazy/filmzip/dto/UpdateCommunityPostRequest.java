package com.crazy.filmzip.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UpdateCommunityPostRequest {
    private String title;
    private String content;
}
