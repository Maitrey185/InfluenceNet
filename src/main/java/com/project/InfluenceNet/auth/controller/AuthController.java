package com.project.InfluenceNet.auth.controller;

import com.project.InfluenceNet.auth.dto.*;
import com.project.InfluenceNet.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.oauth2.jwt.Jwt;
//import org.springframework.web.bind.annotation.*;

import java.util.Map;

//@Slf4j
//@RestController
//@RequestMapping("/api/auth")
//@RequiredArgsConstructor
//@Tag(name = "Authentication", description = "Authentication and user management endpoints")
public class AuthController {
    
//    private final AuthService authService;
//
//    @PostMapping("/register")
//    @Operation(summary = "Register a new user", description = "Create a new user account with role (INFLUENCER or BRAND)")
//    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
//        log.info("Registration request for username: {}", request.getUsername());
//
//        try {
//            AuthResponse response = authService.register(request);
//            return ResponseEntity.status(HttpStatus.CREATED).body(response);
//        } catch (Exception e) {
//            log.error("Registration failed", e);
//            throw new RuntimeException("Registration failed: " + e.getMessage());
//        }
//    }
//
//    @PostMapping("/login")
//    @Operation(summary = "User login", description = "Authenticate user and return JWT tokens")
//    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
//        log.info("Login request for user: {}", request.getUsernameOrEmail());
//
//        try {
//            AuthResponse response = authService.login(request);
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            log.error("Login failed", e);
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body(null);
//        }
//    }
//
//    @PostMapping("/refresh")
//    @Operation(summary = "Refresh access token", description = "Get a new access token using refresh token")
//    public ResponseEntity<AuthResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
//        log.info("Token refresh request");
//
//        try {
//            AuthResponse response = authService.refreshToken(request);
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            log.error("Token refresh failed", e);
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body(null);
//        }
//    }
//
//    @GetMapping("/me")
//    @Operation(summary = "Get current user", description = "Get authenticated user information")
//    public ResponseEntity<AuthResponse.UserInfo> getCurrentUser(Authentication authentication) {
//        log.info("Get current user request");
//
//        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt)) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
//        }
//
//        Jwt jwt = (Jwt) authentication.getPrincipal();
//        String userId = jwt.getSubject();
//
//        try {
//            AuthResponse.UserInfo userInfo = authService.getCurrentUser(userId);
//            return ResponseEntity.ok(userInfo);
//        } catch (Exception e) {
//            log.error("Failed to get current user", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }
//
//    @PostMapping("/forgot-password")
//    @Operation(summary = "Forgot password", description = "Send password reset email to user")
//    public ResponseEntity<Map<String, String>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
//        log.info("Forgot password request for email: {}", request.getEmail());
//
//        try {
//            authService.forgotPassword(request);
//            return ResponseEntity.ok(Map.of(
//                    "message", "If the email exists, a password reset link has been sent"
//            ));
//        } catch (Exception e) {
//            log.error("Forgot password failed", e);
//            // Don't reveal if email exists or not
//            return ResponseEntity.ok(Map.of(
//                    "message", "If the email exists, a password reset link has been sent"
//            ));
//        }
//    }
//
//    @PostMapping("/reset-password")
//    @Operation(summary = "Reset password", description = "Reset password using token from email")
//    public ResponseEntity<Map<String, String>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
//        log.info("Reset password request");
//
//        try {
//            authService.resetPassword(request);
//            return ResponseEntity.ok(Map.of(
//                    "message", "Password reset successfully"
//            ));
//        } catch (UnsupportedOperationException e) {
//            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(Map.of(
//                    "message", e.getMessage()
//            ));
//        } catch (Exception e) {
//            log.error("Reset password failed", e);
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
//                    "message", "Failed to reset password: " + e.getMessage()
//            ));
//        }
//    }
//
//    @PostMapping("/logout")
//    @Operation(summary = "Logout", description = "Logout user (client should discard tokens)")
//    public ResponseEntity<Map<String, String>> logout() {
//        // In a stateless JWT setup, logout is handled client-side by discarding tokens
//        // Optionally, you can implement token blacklisting here
//        log.info("Logout request");
//
//        return ResponseEntity.ok(Map.of(
//                "message", "Logged out successfully. Please discard your tokens."
//        ));
//    }
}
