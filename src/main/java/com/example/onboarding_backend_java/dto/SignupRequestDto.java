package com.example.onboarding_backend_java.dto;

import jakarta.validation.constraints.NotNull;

public record SignupRequestDto(
        @NotNull(message = "아이디는 필수 입력 사항입니다.")
        String username,

        @NotNull(message = "비밀번호를 입력해주세요.")
        String password,

        @NotNull(message = "닉네임을 입력해주세요.")
        String nickname
) {
}
