package com.example.onboarding_backend_java.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class SignRequestDto {
    private String username;
    private String password;
}
