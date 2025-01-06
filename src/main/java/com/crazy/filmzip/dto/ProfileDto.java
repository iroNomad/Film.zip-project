/**
 * 김진원
 */
package com.crazy.filmzip.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProfileDto {
    private String profileImage;
    private String email;
    private String name;
    private String birth;
    private String nickname;
    private String genre;

    public ProfileDto(String profileImage, String email, String name, String birth, String nickname, String genre) {
        this.profileImage = profileImage;
        this.email = email;
        this.name = name;
        this.birth = birth;
        this.nickname = nickname;
        this.genre = genre;
    }
}