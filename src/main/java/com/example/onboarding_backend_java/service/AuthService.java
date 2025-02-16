package com.example.onboarding_backend_java.service;

import com.example.onboarding_backend_java.dto.*;
import com.example.onboarding_backend_java.entity.Role;
import com.example.onboarding_backend_java.entity.User;
import com.example.onboarding_backend_java.repository.UserRepository;
import com.example.onboarding_backend_java.security.CustomUserDetails;
import com.example.onboarding_backend_java.security.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

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

    public SignResponseDto signProcess(SignRequestDto signRequestDto) {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            signRequestDto.username(),
                            signRequestDto.password()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            String role = userDetails.getAuthorities().stream()
                    .findFirst()
                    .map(GrantedAuthority::getAuthority)
                    .orElse("ROLE_USER");

            String accessToken = jwtUtil.createToken("access", userDetails.getUsername(), role, 600000L);
            return new SignResponseDto(accessToken);
    }

}
