package com.tecsup.formalizape.auth.service;

import com.tecsup.formalizape.auth.model.User;
import com.tecsup.formalizape.auth.repository.UserRepository;
import com.tecsup.formalizape.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    public String register(String username, String password) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Usuario ya existe");
        }

        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .enabled(true)
                .build();

        userRepository.save(user);
        return jwtService.generateToken(Map.of(), username);
    }

    public String login(String username, String password) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        return jwtService.generateToken(Map.of(), username);
    }
}
