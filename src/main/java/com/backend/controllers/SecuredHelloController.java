package com.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class SecuredHelloController {

    @GetMapping("/helloUser")
    public ResponseEntity<Map<String, String>> helloUser() {
        return ResponseEntity.ok(Map.of("message", "helloUser"));
    }

    @GetMapping("/helloAdmin")
    public ResponseEntity<Map<String, String>> helloAdmin() {
        return ResponseEntity.ok(Map.of("message", "helloAdmin"));
    }
}
