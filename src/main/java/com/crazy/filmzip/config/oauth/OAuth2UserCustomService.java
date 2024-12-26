package com.crazy.filmzip.config.oauth;

import com.crazy.filmzip.entity.User;
import com.crazy.filmzip.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Log4j2
@Service
public class OAuth2UserCustomService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // 요청을 바탕으로 유저 정보를 담은 객체 반환
        OAuth2User user = super.loadUser(userRequest);
        String provider = userRequest.getClientRegistration().getRegistrationId();

        saveOrUpdate(user, provider);

        return user;
    }

    // 유저가 있으면 업데이트, 없으면 유저 생성
    private User saveOrUpdate(OAuth2User oAuth2User, String provider) {
        String email;
        String name;

        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 네이버
        if("naver".equals(provider)) {
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");
            email = (String) response.get("email");
            name = (String) response.get("name");
        }else if("google".equals(provider)) {
            email = (String) attributes.get("email");
            name = (String) attributes.get("name");
        }else{
            email = null;
            name = null;
        }

        User user = userRepository.findByEmail(email)
                .map(entity -> entity.update(name))
                .orElse(User.builder()
                        .email(email)
                        .nickname(name)
                        .build());

        return userRepository.save(user);
    }
}
