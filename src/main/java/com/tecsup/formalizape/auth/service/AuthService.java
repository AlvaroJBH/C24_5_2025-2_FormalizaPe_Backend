package com.tecsup.formalizape.auth.service;

import com.tecsup.formalizape.auth.model.User;
import com.tecsup.formalizape.auth.repository.UserRepository;
import com.tecsup.formalizape.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public String register(String username, String email, String password, String dni, String ruc) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email ya registrado");
        }
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Nombre de empresa ya registrado");
        }

        User user = User.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .dni(dni)
                .ruc(ruc)
                .roles(new HashSet<>())
                .enabled(true)
                .build();

        userRepository.save(user);

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("username", user.getUsername());

        return jwtService.generateToken(extraClaims, user.getEmail());
    }

    public String login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email no encontrado"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("username", user.getUsername());

        return jwtService.generateToken(extraClaims, user.getEmail());
    }
}
