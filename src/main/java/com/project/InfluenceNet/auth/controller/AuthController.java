package com.project.InfluenceNet.auth.controller;

import com.project.InfluenceNet.auth.dto.*;
import com.project.InfluenceNet.auth.entity.User;
import com.project.InfluenceNet.auth.service.AuthService;
import com.project.InfluenceNet.auth.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.oauth2.jwt.Jwt;
//import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication and user management endpoints")
public class AuthController {
    

    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Operation(summary = "Register a new user")
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        log.info("Auth register request received for username={}", registerRequest.getUsername());
        registerRequest.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        User user = authService.registerUser(registerRequest);
        log.info("Auth register succeeded for username={} userId={}", user.getUsername(), user.getId());
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<String> generateToken(@Valid @RequestBody LoginRequest loginRequest) {

        log.info("Auth login attempt for username={}", loginRequest.getUsername());

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
        } catch (Exception ex) {
            log.warn("Auth login failed for username={}: {}", loginRequest.getUsername(), ex.getClass().getSimpleName());
            throw ex;
        }

        if (authentication.isAuthenticated()) {
            // Generate token if authentication successful
            log.info("Auth login succeeded for username={}", loginRequest.getUsername());
            return ResponseEntity.ok(jwtUtil.generateToken(loginRequest.getUsername(), 15));
        } else {
            log.warn("Auth login rejected (not authenticated) for username={}", loginRequest.getUsername());
            throw new RuntimeException("Invalid credentials");
        }
//        return ResponseEntity.ok(authService.login(loginRequest.getUsername(), loginRequest.getPassword()));
    }

    @PostMapping("/check")
    public ResponseEntity<String> check() {
        return ResponseEntity.ok("Check successful");
    }
}
