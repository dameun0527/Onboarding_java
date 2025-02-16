package com.example.onboarding_backend_java.config;

import com.example.onboarding_backend_java.security.jwt.JWTAuthenticationFilter;
import com.example.onboarding_backend_java.security.jwt.JWTAuthorizationFilter;
import com.example.onboarding_backend_java.security.jwt.JWTUtil;
import com.example.onboarding_backend_java.security.jwt.refresh.RefreshService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;
    private final RefreshService refreshService;



    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        JWTAuthenticationFilter jwtFilter =
                new JWTAuthenticationFilter(authenticationManager(authenticationConfiguration), jwtUtil, refreshService);
        jwtFilter.setFilterProcessesUrl("/sign");

        http
                .csrf((auth) -> auth.disable())
                .formLogin((auth) -> auth.disable())
                .httpBasic((auth) -> auth.disable())
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/sign", "/", "/signup", "/refresh").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(new JWTAuthorizationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
