package com.crazy.filmzip.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddUserRequest {

    private String email;
    private String password;
    private Integer birth;
    private String name;
    private String nickname;
    private String profileImage;
}
