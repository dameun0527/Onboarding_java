package com.example.onboarding_backend_java.dto;

import java.util.List;

public record SignupResponseDto(
        String username,
        String nickname,
        List<AuthorityDto> authorities
) {
}
