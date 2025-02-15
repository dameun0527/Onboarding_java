package com.example.onboarding_backend_java.security.jwt;

import com.example.onboarding_backend_java.entity.Role;
import com.example.onboarding_backend_java.entity.User;
import com.example.onboarding_backend_java.security.CustomUserDetails;
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

            String authorization = request.getHeader("Authorization");

            if (authorization == null || !authorization.startsWith("Bearer ")) {

                log.info("token null");
                filterChain.doFilter(request, response);
                return;
            }
            log.info("authorization now");

            String token = authorization.substring(7);

            if (jwtUtil.isExpired(token)) {
                log.info("token expired");
                filterChain.doFilter(request, response);
                return;
            }

            String username = jwtUtil.getUsername(token);
            String role = jwtUtil.getRole(token);

            User user = new User();
            user.setUsername(username);
            user.setPassword("temppassword");
            user.setRole(Role.valueOf(role));

            CustomUserDetails customUserDetails = new CustomUserDetails(user);
            Authentication authToken = new UsernamePasswordAuthenticationToken(
                    customUserDetails,
                    null,
                    customUserDetails.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(authToken);

            filterChain.doFilter(request, response);
        } catch(JwtException e) {
            throw e;
        } catch (Exception e) {
            log.error("인가 중 서버 및 내부 오류 발생: ", e.getMessage(), e);
            throw e;
        }
    }
}
