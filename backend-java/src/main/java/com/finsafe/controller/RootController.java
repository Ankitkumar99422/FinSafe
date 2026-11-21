package com.finsafe.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RootController {

    @GetMapping("/")
    public ResponseEntity<Map<String, String>> root() {
        return ResponseEntity.ok(Map.of(
                "service", "FinSafe Spring Boot Core Backend",
                "status", "UP",
                "documentation", "/swagger-ui/index.html",
                "health", "/api/health"
        ));
    }
}
