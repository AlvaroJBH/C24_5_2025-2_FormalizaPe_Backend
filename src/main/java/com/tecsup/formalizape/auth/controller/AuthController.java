package com.tecsup.formalizape.auth.controller;

import com.tecsup.formalizape.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody Map<String, String> request) {
        String token = authService.register(
                request.get("username"),   // nombre de empresa
                request.get("email"),
                request.get("password"),
                request.get("dni"),
                request.get("ruc")
        );
        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> request) {
        String token = authService.login(
                request.get("email"),
                request.get("password")
        );
        return ResponseEntity.ok(Map.of("token", token));
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> me(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(Map.of(
                "username", user.getUsername(),
                "roles", user.getAuthorities()
        ));
    }
}
