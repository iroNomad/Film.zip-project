/**
 * 자카
 */
package com.crazy.filmzip.dto;

import com.crazy.filmzip.entity.ToWatchMovie;
import com.crazy.filmzip.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddWatchListRequest {
    // 데이터를 처리 (요청용)
    private Long userId;
    private Integer movieApiId;
    private String title;
    private String backdropPath;

    public ToWatchMovie toEntity(User user) {
        return ToWatchMovie.builder()
                .user(user)
                .movieApiId(movieApiId)
                .title(title)
                .backdropPath(backdropPath)
                .build();
    }

    @Override
    public String toString() {
        return "AddWatchListRequest{" +
                "userId=" + userId +
                ", movieApiId=" + movieApiId +
                ", title='" + title + '\'' +
                ", backdropPath='" + backdropPath + '\'' +
                '}';
    }
}
