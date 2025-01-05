package com.crazy.filmzip.controller;

import com.crazy.filmzip.dto.ProfileDto;
import com.crazy.filmzip.entity.User;
import com.crazy.filmzip.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import oracle.jdbc.proxy.annotation.Post;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

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
