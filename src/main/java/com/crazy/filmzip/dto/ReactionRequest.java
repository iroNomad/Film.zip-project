/**
 * 장다은
 */
package com.crazy.filmzip.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReactionRequest {
    private Long commentId;
    private String reaction; // "recommend" 또는 "notRecommend"
    // Getters and Setters
}
