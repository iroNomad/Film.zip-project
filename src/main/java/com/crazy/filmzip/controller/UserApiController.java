package com.crazy.filmzip.controller;

import com.crazy.filmzip.dto.AddUserRequest;
import com.crazy.filmzip.dto.ProfileDto;
import com.crazy.filmzip.entity.User;
import com.crazy.filmzip.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@Controller
public class UserApiController {

    private final UserService userService;

    @PostMapping("/user")
    public String signup(AddUserRequest request) {
        userService.save(request);
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login";
    }

    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestParam String email) {
        boolean exists = userService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/check-nickname")
    public ResponseEntity<Boolean> checkNickname(@RequestParam String nickname) {
        boolean exists = userService.existsByNickname(nickname);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/api/user/mypage")
    public ResponseEntity<ProfileDto> mypage() {
        User user = getCurrentUser();

        // 프로필 이미지 경로 완성
        String profileImageUrl = "/profile/" + (user.getProfileImage() != null ? user.getProfileImage() : "profile0.png");

        ProfileDto profileDto = new ProfileDto(
                profileImageUrl,
                user.getEmail(),
                user.getName(),
                String.valueOf(user.getBirth()),
                user.getNickname(),
                user.getGenre()
        );

        return ResponseEntity.ok(profileDto);
    }

    // 현재 인증된 사용자 가져오기
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authentication: " + authentication);

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("현재 사용자 인증 정보가 없습니다.");
        }

        String email = authentication.getName();
        System.out.println("Authenticated User Email: " + email);

        return userService.findByEmail(email);
    }
}
