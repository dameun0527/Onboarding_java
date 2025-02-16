package com.example.onboarding_backend_java.controller;

import com.example.onboarding_backend_java.service.TestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    @GetMapping
    public String testEndpoint() {
        log.info("[TestController] /api/test GET called");

        String result = testService.doSomething();

        return "Test Endpoint: " + result;
    }
}
