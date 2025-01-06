/**
 * 김진원
 */
package com.crazy.filmzip.controller;

import com.crazy.filmzip.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Log4j2
public class UserViewController {

    private final UserService userService;

    // 로그인
    @GetMapping("/login")
    public String login() {

        return "login";
    }

    // 회원가입
    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    // 마이페이지
    @GetMapping("/user/mypage")
    public String mypage() {
        return "user/mypage";
    }

    // 프로필 수정
    @GetMapping("/user/modify")
    public String modify() {
        return "user/modify";
    }
}
