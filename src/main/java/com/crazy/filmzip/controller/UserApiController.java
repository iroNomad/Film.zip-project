/**
 * 김진원
 */
package com.crazy.filmzip.controller;

import com.crazy.filmzip.dto.AddUserRequest;
import com.crazy.filmzip.dto.ProfileDto;
import com.crazy.filmzip.dto.ProfileUpdateRequest;
import com.crazy.filmzip.entity.User;
import com.crazy.filmzip.service.RefreshTokenService;
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
import org.springframework.web.bind.annotation.*;

@Log4j2
@RequiredArgsConstructor
@Controller
public class UserApiController {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    // 회원가입
    @PostMapping("/user")
    public String signup(AddUserRequest request) {
        userService.save(request);
        return "redirect:/login";
    }

    // 로그아웃
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login";
    }

    // 이메일 중복 검사
    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestParam String email) {
        boolean exists = userService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }

    // 닉네임 중복 검사
    @GetMapping("/check-nickname")
    public ResponseEntity<Boolean> checkNickname(@RequestParam String nickname) {
        boolean exists = userService.existsByNickname(nickname);
        return ResponseEntity.ok(exists);
    }

    // 마이페이지
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

    // 프로필 수정
    @PostMapping("/api/user/modify")
    public ResponseEntity<String> modifyProfile(@RequestBody ProfileUpdateRequest request) {
        User user = getCurrentUser();

        // 프로필 이미지 수정
        if (request.getProfileImage() != null) {
            // 예: "/profile/profile0.png" -> "profile0.png"
            String profileImage = request.getProfileImage();
            if (profileImage.startsWith("/profile/")) {
                profileImage = profileImage.substring("/profile/".length());
            }
            user.setProfileImage(profileImage);
        }

        // 닉네임 수정
        if (request.getNickname() != null) {
            user.setNickname(request.getNickname());
        }

        // 영화 장르 수정
        if (request.getGenre() != null) {
            user.setGenre(request.getGenre());
        }

        userService.modifyProfile(user);

        return ResponseEntity.ok("프로필이 성공적으로 수정되었습니다.");
    }

    // 회원 탈퇴
    @PostMapping("/api/user/delete")
    public ResponseEntity<String> deleteUser() {
        User currentUser = getCurrentUser(); // 현재 인증된 사용자
        userService.deleteUser(currentUser); // DB에서 삭제
        refreshTokenService.delete();

        // 필요하다면 세션 무효화나 추가 처리
        return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
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
