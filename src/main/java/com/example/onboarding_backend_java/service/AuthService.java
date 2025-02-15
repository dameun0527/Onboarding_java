package com.example.onboarding_backend_java.service;

import com.example.onboarding_backend_java.dto.AuthorityDto;
import com.example.onboarding_backend_java.dto.SignupRequestDto;
import com.example.onboarding_backend_java.dto.SignupResponseDto;
import com.example.onboarding_backend_java.entity.Role;
import com.example.onboarding_backend_java.entity.User;
import com.example.onboarding_backend_java.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final PasswordEncoder passwordEncoder;

    public SignupResponseDto signupProcess(SignupRequestDto signupRequestDto) {
        User user = User.builder()
                .username(signupRequestDto.username())
                .password(passwordEncoder.encode(signupRequestDto.password()))
                .nickname(signupRequestDto.nickname())
                .role(Role.ROLE_USER)
                .build();

        User savedUser = userRepository.save(user);

        AuthorityDto authority = new AuthorityDto(savedUser.getRole().name());
        List<AuthorityDto> authorities = Collections.singletonList(authority);

        return new SignupResponseDto(savedUser.getUsername(), savedUser.getNickname(), authorities);
    }
}
