package com.example.onboarding_backend_java.security.jwt;

import com.example.onboarding_backend_java.entity.Role;
import com.example.onboarding_backend_java.entity.User;
import com.example.onboarding_backend_java.security.CustomUserDetails;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {

            String accessToken = request.getHeader("access");

            if (accessToken == null) {
                filterChain.doFilter(request, response);
                return;
            }

            try {
                jwtUtil.isExpired(accessToken);
            } catch (ExpiredJwtException e) {
                log.warn("토큰이 만료되었습니다.");

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"error\": \"토큰 만료\"}");
                return;
            }

            String category = jwtUtil.getCategory(accessToken);
            if (!"access".equals(category)) {
                log.warn("유효하지 않은 토큰입니다.");

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"error\": \"유효하지 않은 토큰\"}");
                return;
            }

            String username = jwtUtil.getUsername(accessToken);
            String role = jwtUtil.getRole(accessToken);

            User user = new User();
            user.setUsername(username);
            user.setRole(Role.valueOf(role));

            CustomUserDetails customUserDetails = new CustomUserDetails(user);

            Authentication authToken = new UsernamePasswordAuthenticationToken(
                    customUserDetails,
                    null,
                    customUserDetails.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(authToken);
            log.info("User authenticated - username: {}, role: {}", username, role);

            filterChain.doFilter(request, response);

        } catch (JwtException e) {
            log.error("인가 중 서버 및 내부 오류 발생: ", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"error\": \"Internal server error\"}");
        }
    }
}
