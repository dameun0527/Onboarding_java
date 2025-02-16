package com.example.onboarding_backend_java.security.jwt;

import com.example.onboarding_backend_java.dto.SignRequestDto;
import com.example.onboarding_backend_java.security.jwt.refresh.RefreshService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import static com.example.onboarding_backend_java.security.jwt.refresh.CookieUtil.createCookie;

//@Slf4j
//@RequiredArgsConstructor
//public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
//
//    private final AuthenticationManager authenticationManager;
//    private final JWTUtil jwtUtil;
//    private final RefreshService refreshService;
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    @Override
//    public Authentication attemptAuthentication(
//            HttpServletRequest request,
//            HttpServletResponse response) throws AuthenticationException {
//        try {
//            SignRequestDto signRequestDto = objectMapper.readValue(request.getInputStream(), SignRequestDto.class);
//            String username = signRequestDto.getUsername();
//            String password = signRequestDto.getPassword();
//            log.info("attemptAuthentication: username={}, password={}", username, password);
//
//            UsernamePasswordAuthenticationToken authToken =
//                    new UsernamePasswordAuthenticationToken(username, password, null);
//
//            return authenticationManager.authenticate(authToken);
//        } catch (IOException e) {
//            log.error("attemptAuthentication: {}", e.getMessage());
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    protected void successfulAuthentication(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            FilterChain chain,
//            Authentication authentication) throws IOException {
//
//        String username = authentication.getName();
//
//        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
//        GrantedAuthority auth = iterator.next();
//
//        String role = auth.getAuthority();
//        log.info("successfulAuthentication: username={}, role={}", username, role);
//
//        String accessToken = jwtUtil.createToken("access", username, role, 600000L);
//        log.info("Generated accessToken: {}", accessToken);
//
//        String refreshToken = jwtUtil.createToken("refresh", username, role, 86400000L);
//        log.info("Generated refreshToken: {}", refreshToken);
//
//        refreshService.deleteRefreshToken(refreshToken);
//        refreshService.saveRefreshToken(username, refreshToken, 86400000L);
//
//        response.setHeader("access", accessToken);
//        response.addCookie(createCookie("refresh", refreshToken));
//        response.setStatus(HttpStatus.OK.value());
//    }
//
//    @Override
//    protected void unsuccessfulAuthentication(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            AuthenticationException failed) throws IOException {
//        log.error("unsuccessfulAuthentication: {}", failed.getMessage());
//
//        response.setStatus(401);
//        response.setContentType("application/json;charset=UTF-8");
//        response.getWriter().write("{\"error\": \"자격 증명에 실패하였습니다.\"}");
//    }
//
//}
