/**
 * 이지민
 */
package com.crazy.filmzip.dto;

import com.crazy.filmzip.entity.CommunityPost;
import com.crazy.filmzip.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddCommunityPostRequest {
    // 데이터를 처리 (요청용)
    private Long userId;
    private String title;
    private String content;

    public CommunityPost toEntity(User user) {
        return CommunityPost.builder()
                .title(title)
                .content(content)
                .user(user)
                .views(0)
                .build();
    }
}
