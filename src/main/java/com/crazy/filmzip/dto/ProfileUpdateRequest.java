package com.crazy.filmzip.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProfileUpdateRequest {
    private String profileImage; // 프로필 이미지 파일명
    private String nickname;     // 닉네임
    private String genre;        // 영화 장르
}