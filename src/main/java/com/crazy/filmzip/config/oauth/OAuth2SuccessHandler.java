package com.crazy.filmzip.config.oauth;

import com.crazy.filmzip.config.jwt.TokenProvider;
import com.crazy.filmzip.entity.RefreshToken;
import com.crazy.filmzip.entity.User;
import com.crazy.filmzip.repository.RefreshTokenRepository;
import com.crazy.filmzip.service.UserService;
import com.crazy.filmzip.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14);
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofDays(1);
    public static final String REDIRECT_PATH = "/main";

    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;
    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        String email;

        // provider 정보 확인
        String provider = null;
        if (authentication instanceof org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken) {
            org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken oauthToken =
                    (org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken) authentication;
            provider = oauthToken.getAuthorizedClientRegistrationId();
        }

        System.out.println("provider = " + provider);

        if (provider != null) {

            // OAuth2 로그인 처리
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

            Map<String, Object> attributes = oAuth2User.getAttributes();

            if ("naver".equals(provider)) {
                Map<String, Object> responseAttributes = (Map<String, Object>) attributes.get("response");
                email = (String) responseAttributes.get("email");
            } else if ("google".equals(provider)) {
                email = (String) attributes.get("email");
            } else if ("kakao".equals(provider)) {
                Map<String, Object> kakaoAccountAttributes = (Map<String, Object>) attributes.get("kakao_account");
                email = (String) kakaoAccountAttributes.get("email");
            } else {
                throw new IllegalArgumentException("Unknown OAuth2 Provider: " + provider);
            }

        }else{

            // 일반 로그인 처리
            Object principal = authentication.getPrincipal();

            if (principal instanceof User) {
                User user = (User) principal;
                email = user.getUsername(); // 일반 로그인 사용자의 이메일
            } else {
                throw new IllegalArgumentException("Unknown Authentication Principal: " + principal);
            }
        }

        System.out.println("email = " + email);

        User user = userService.findByEmail(email);

        // 리프레시 토큰 생성 -> 저장 -> 쿠키에 저장
        String refreshToken = tokenProvider.generateToken(user, REFRESH_TOKEN_DURATION);
        saveRefreshToken(user.getId(), refreshToken);
        addRefreshTokenToCookie(request, response, refreshToken);

        // 액세스 토큰 생성 -> 패스에 액세스 토큰 추가
        String accessToken = tokenProvider.generateToken(user, ACCESS_TOKEN_DURATION);
        String targetUrl = getTargetUrl(accessToken);

        // 인증 관련 설정값, 쿠키 제거
        clearAuthenticationAttributes(request, response);

        // 리다이렉트
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    // 생성된 리프레시 토큰을 전달받아 데이터베이스에 저장
    private void saveRefreshToken(Long userId, String newRefreshToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId)
                .map(entity -> entity.update(newRefreshToken))
                .orElse(new RefreshToken(userId, newRefreshToken));

        refreshTokenRepository.save(refreshToken);
    }

    // 생성된 리프레시 토큰을 쿠키에 저장
    private void addRefreshTokenToCookie(HttpServletRequest request, HttpServletResponse response, String refreshToken) {
        int cookieMaxAge = (int) REFRESH_TOKEN_DURATION.toSeconds();

        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN_COOKIE_NAME);
        CookieUtil.addCookie(response, REFRESH_TOKEN_COOKIE_NAME, refreshToken, cookieMaxAge);
    }

    // 인증 관련 설정값, 쿠키 제거
    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    // 액세스 토큰을 패스에 추가
    private String getTargetUrl(String token) {
        return UriComponentsBuilder.fromUriString(REDIRECT_PATH)
                .queryParam("token", token)
                .build()
                .toUriString();
    }
}
