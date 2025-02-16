package com.example.onboarding_backend_java.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


class JWTUtilTest {

    private static final String SECRET_KEY = "pB+EU94ugGpGZeRZh7pPi+aW1Jcv3uE9Zs1y2rBOw3IFJY/1ttMyz3K438sUyzQQdNAluzR3a9x+M0ZRyeuNhw==";

    private JWTUtil jwtUtil;

    @BeforeEach
    void setUp() {
        // 생성자에 secret key 전달하면, 내부적으로 Base64 디코딩 후 Key 생성
        jwtUtil = new JWTUtil(SECRET_KEY);
    }

    @Test
    @DisplayName("Access Token 생성 테스트")
    void testCreateAccessToken() {
        String token = jwtUtil.createToken("access", "testUser", "ROLE_USER", 600000L);
        assertNotNull(token);
        assertEquals("testUser", jwtUtil.getUsername(token));
        assertEquals("ROLE_USER", jwtUtil.getRole(token));
        assertEquals("access", jwtUtil.getCategory(token));
        assertFalse(jwtUtil.isExpired(token));
    }

    @Test
    @DisplayName("Refresh Token 생성 테스트")
    void testCreateRefreshToken() {
        String token = jwtUtil.createToken("refresh", "testUser", "ROLE_USER", 86400000L);
        assertNotNull(token);
        assertEquals("testUser", jwtUtil.getUsername(token));
        assertEquals("ROLE_USER", jwtUtil.getRole(token));
        assertEquals("refresh", jwtUtil.getCategory(token));
        assertFalse(jwtUtil.isExpired(token));
    }

    @Test
    @DisplayName("Access Token 유효성 검증")
    void testValidateAccessToken() {
        String token = jwtUtil.createToken("access", "testUser", "ROLE_USER", 600000L);
        assertFalse(jwtUtil.isExpired(token));
    }

    @Test
    @DisplayName("Refresh Token 유효성 검증")
    void testValidateRefreshToken() {
        String token = jwtUtil.createToken("refresh", "testUser", "ROLE_USER", 86400000L);
        assertFalse(jwtUtil.isExpired(token));
    }

    @Test
    @DisplayName("만료된 Access Token 검증")
    void testExpiredAccessToken() {
        String token = jwtUtil.createToken("access", "testUser", "ROLE_USER", -1000L);
        assertThrows(ExpiredJwtException.class, () -> jwtUtil.isExpired(token));
    }

    @Test
    @DisplayName("만료된 Refresh Token 검증")
    void testExpiredRefreshToken() {
        String token = jwtUtil.createToken("refresh", "testUser", "ROLE_USER", -1000L);
        assertThrows(ExpiredJwtException.class, () -> jwtUtil.isExpired(token));
    }

    @Test
    @DisplayName("Access Token에서 사용자 정보 추출")
    void testExtractUserInfoFromAccessToken() {
        String token = jwtUtil.createToken("access", "testUser", "ROLE_USER", 600000L);
        assertEquals("testUser", jwtUtil.getUsername(token));
        assertEquals("ROLE_USER", jwtUtil.getRole(token));
    }

    @Test
    @DisplayName("Refresh Token에서 사용자 정보 추출")
    void testExtractUserInfoFromRefreshToken() {
        String token = jwtUtil.createToken("refresh", "testUser", "ROLE_USER", 86400000L);
        assertEquals("testUser", jwtUtil.getUsername(token));
        assertEquals("ROLE_USER", jwtUtil.getRole(token));
    }

    @Test
    @DisplayName("손상된 토큰")
    void testTamperedToken() {
        String token = jwtUtil.createToken("access", "testUser", "ROLE_USER", 600000L);

        String[] parts = token.split("\\.");
        assertEquals(3, parts.length, "토큰은 3개의 부분으로 구성되어야 함");

        String tamperedSignature = parts[2].substring(0, parts[2].length() - 1) + "a";
        String tamperedToken = parts[0] + "." + parts[1] + "." + tamperedSignature;

        assertThrows(JwtException.class, () -> jwtUtil.getUsername(tamperedToken));
    }
}