package com.example.onboarding_backend_java;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableWebSecurity
public class OnboardingBackendJavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnboardingBackendJavaApplication.class, args);
    }

}
