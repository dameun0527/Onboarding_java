package com.example.onboarding_backend_java.security.jwt.refresh;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Refresh {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String refreshToken;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiration;

    public static Refresh create(String username, String refreshToken, Date expiration) {
        return Refresh.builder()
                .username(username)
                .refreshToken(refreshToken)
                .expiration(expiration)
                .build();
    }
}
