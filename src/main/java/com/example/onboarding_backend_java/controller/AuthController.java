package com.example.onboarding_backend_java.controller;

import com.example.onboarding_backend_java.dto.SignRequestDto;
import com.example.onboarding_backend_java.dto.SignResponseDto;
import com.example.onboarding_backend_java.dto.SignupRequestDto;
import com.example.onboarding_backend_java.dto.SignupResponseDto;
import com.example.onboarding_backend_java.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(@RequestBody SignupRequestDto signupRequestDto) {
        SignupResponseDto responseDto = authService.signupProcess(signupRequestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/sign")
    public SignResponseDto signProcess(@RequestBody SignRequestDto signRequestDto) {
        return authService.signProcess(signRequestDto);
    }
}
