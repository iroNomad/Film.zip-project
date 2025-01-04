package com.crazy.filmzip.service;

import com.crazy.filmzip.dto.AddUserRequest;
import com.crazy.filmzip.entity.User;
import com.crazy.filmzip.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public Long save(AddUserRequest dto) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        return userRepository.save(User.builder()
                .email(dto.getEmail())
                .password(encoder.encode(dto.getPassword()))
                .birth(dto.getBirth())
                .name(dto.getName())
                .nickname(dto.getNickname())
                .profileImage(dto.getProfileImage())
                .genre(dto.getGenre())
                .build()).getId();
    }

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean existsByNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    public Long modifyProfile(User user) {
        return userRepository.save(user).getId();
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }
}
