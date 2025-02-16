package com.example.onboarding_backend_java.security.jwt.refresh;

import com.example.onboarding_backend_java.security.jwt.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.onboarding_backend_java.security.jwt.refresh.CookieUtil.createCookie;

@RestController
@RequiredArgsConstructor
public class RefreshController {

    private final JWTUtil jwtUtil;
    private final RefreshService refreshService;

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response) {

        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        if (refreshToken == null) {
            return new ResponseEntity<>("refreshToken이 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        if (!refreshService.existsByRefreshToken(refreshToken)) {
            return new ResponseEntity<>("존재하지 않는 refreshToken입니다.", HttpStatus.BAD_REQUEST);
        }

        try {
            jwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {
            refreshService.deleteRefreshToken(refreshToken);
            return new ResponseEntity<>("refreshToken이 만료되었습니다.", HttpStatus.UNAUTHORIZED);
        }

        String category = jwtUtil.getCategory(refreshToken);

        if (!"refresh".equals(category)) {
            return new ResponseEntity<>("유효하지 않은 refreshToken입니다.", HttpStatus.BAD_REQUEST);
        }

        String username = jwtUtil.getUsername(refreshToken);
        String role = jwtUtil.getRole(refreshToken);

        String newAccessToken = jwtUtil.createToken("access", username, role, 600000L);
        String newRefreshToken = jwtUtil.createToken("refresh", username, role, 86400000L);

        refreshService.deleteRefreshToken(refreshToken);
        refreshService.saveRefreshToken(username, newRefreshToken, 86400000L);

        response.setHeader("access", newAccessToken);
        response.addCookie(createCookie("refresh", newRefreshToken));

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
