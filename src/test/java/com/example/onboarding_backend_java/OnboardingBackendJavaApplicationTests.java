package com.example.onboarding_backend_java;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class OnboardingBackendJavaApplicationTests {

    @Test
    void contextLoads() {
        System.out.println("Test is running");
    }

}
