package com.example.onboarding_backend_java.security.jwt.refresh;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class RefreshService {

    private final RefreshRepository refreshRepository;

    @Transactional
    public void saveRefreshToken(String username, String refreshToken, Long expiredMs) {
        Date expirationDate = new Date(System.currentTimeMillis() + expiredMs);

        refreshRepository.deleteByRefreshToken(refreshToken);

        Refresh refresh = Refresh.create(username, refreshToken, expirationDate);
        refreshRepository.save(refresh);
    }

    public boolean existsByRefreshToken(String refreshToken) {
        return refreshRepository.existsByRefreshToken(refreshToken);
    }

    @Transactional
    public void deleteRefreshToken(String refreshToken) {
        refreshRepository.deleteByRefreshToken(refreshToken);
    }
}
