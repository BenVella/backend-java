package com.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AccessProbeController {

    @GetMapping("/api/ping")
    public ResponseEntity<Map<String, String>> ping() {
        return ResponseEntity.ok(Map.of("status", "ok"));
    }

    @GetMapping("/api/access/public")
    public ResponseEntity<Map<String, String>> publicAccess() {
        return ResponseEntity.ok(Map.of("access", "public"));
    }

    @GetMapping("/api/access/user")
    public ResponseEntity<Map<String, String>> userAccess() {
        return ResponseEntity.ok(Map.of("access", "user"));
    }

    @GetMapping("/api/access/admin")
    public ResponseEntity<Map<String, String>> adminAccess() {
        return ResponseEntity.ok(Map.of("access", "admin"));
    }
}
