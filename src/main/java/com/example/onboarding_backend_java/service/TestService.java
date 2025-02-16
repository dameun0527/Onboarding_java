package com.example.onboarding_backend_java.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TestService {

    public String doSomething() {
        log.info("[TestService] doSomething called");

        return "Hello World!";
    }
}
