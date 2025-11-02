package com.project.InfluenceNet.auth.service;

import com.project.InfluenceNet.auth.dto.*;
//import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
//import org.keycloak.admin.client.Keycloak;
//import org.keycloak.admin.client.resource.RealmResource;
//import org.keycloak.admin.client.resource.UserResource;
//import org.keycloak.admin.client.resource.UsersResource;
//import org.keycloak.representations.idm.CredentialRepresentation;
//import org.keycloak.representations.idm.RoleRepresentation;
//import org.keycloak.representations.idm.UserRepresentation;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.*;
import org.springframework.stereotype.Service;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    
//    private final Keycloak keycloak;
//    private final RestTemplate restTemplate;
//
//    @Value("${app.keycloak.server-url}")
//    private String serverUrl;
//
//    @Value("${app.keycloak.realm}")
//    private String realm;
//
//    @Value("${app.keycloak.client-id}")
//    private String clientId;
//
//    @Value("${app.keycloak.client-secret}")
//    private String clientSecret;
//
//    /**
//     * Register a new user in Keycloak
//     */
//    public AuthResponse register(RegisterRequest request) {
//        log.info("Registering new user: {}", request.getUsername());
//
//        RealmResource realmResource = keycloak.realm(realm);
//        UsersResource usersResource = realmResource.users();
//
//        // Check if user already exists
//        List<UserRepresentation> existingUsers = usersResource.search(request.getUsername());
//        if (!existingUsers.isEmpty()) {
//            throw new RuntimeException("Username already exists");
//        }
//
//        // Check if email already exists
//        existingUsers = usersResource.searchByEmail(request.getEmail(), true);
//        if (!existingUsers.isEmpty()) {
//            throw new RuntimeException("Email already exists");
//        }
//
//        // Create user representation
//        UserRepresentation user = new UserRepresentation();
//        user.setUsername(request.getUsername());
//        user.setEmail(request.getEmail());
//        user.setFirstName(request.getFirstName());
//        user.setLastName(request.getLastName());
//        user.setEnabled(true);
//        user.setEmailVerified(false);
//
//        // Create user
//        Response response = usersResource.create(user);
//
//        if (response.getStatus() != 201) {
//            log.error("Failed to create user. Status: {}", response.getStatus());
//            throw new RuntimeException("Failed to create user: " + response.getStatusInfo());
//        }
//
//        // Get user ID from location header
//        String locationHeader = response.getHeaderString("Location");
//        String userId = locationHeader.substring(locationHeader.lastIndexOf('/') + 1);
//
//        log.info("User created with ID: {}", userId);
//
//        // Set password
//        UserResource userResource = usersResource.get(userId);
//        CredentialRepresentation credential = new CredentialRepresentation();
//        credential.setType(CredentialRepresentation.PASSWORD);
//        credential.setValue(request.getPassword());
//        credential.setTemporary(false);
//        userResource.resetPassword(credential);
//
//        // Assign role
//        assignRole(userResource, realmResource, request.getRole());
//
//        // Send verification email
//        try {
//            userResource.sendVerifyEmail();
//            log.info("Verification email sent to: {}", request.getEmail());
//        } catch (Exception e) {
//            log.warn("Failed to send verification email: {}", e.getMessage());
//        }
//
//        // Login and return tokens
//        return login(LoginRequest.builder()
//                .usernameOrEmail(request.getUsername())
//                .password(request.getPassword())
//                .build());
//    }
//
//    /**
//     * Login user and get JWT tokens
//     */
//    public AuthResponse login(LoginRequest request) {
//        log.info("User login attempt: {}", request.getUsernameOrEmail());
//
//        String tokenUrl = serverUrl + "/realms/" + realm + "/protocol/openid-connect/token";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//        body.add("grant_type", "password");
//        body.add("client_id", clientId);
//        body.add("client_secret", clientSecret);
//        body.add("username", request.getUsernameOrEmail());
//        body.add("password", request.getPassword());
//        body.add("scope", "openid profile email");
//
//        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);
//
//        try {
//            ResponseEntity<Map> response = restTemplate.exchange(
//                    tokenUrl,
//                    HttpMethod.POST,
//                    requestEntity,
//                    Map.class
//            );
//
//            Map<String, Object> tokenResponse = response.getBody();
//
//            if (tokenResponse == null) {
//                throw new RuntimeException("Failed to get token response");
//            }
//
//            // Get user info
//            UserRepresentation user = getUserByUsername(request.getUsernameOrEmail());
//
//            return AuthResponse.builder()
//                    .accessToken((String) tokenResponse.get("access_token"))
//                    .refreshToken((String) tokenResponse.get("refresh_token"))
//                    .tokenType("Bearer")
//                    .expiresIn(((Number) tokenResponse.get("expires_in")).longValue())
//                    .user(mapToUserInfo(user))
//                    .build();
//
//        } catch (Exception e) {
//            log.error("Login failed for user: {}", request.getUsernameOrEmail(), e);
//            throw new RuntimeException("Invalid credentials");
//        }
//    }
//
//    /**
//     * Refresh access token
//     */
//    public AuthResponse refreshToken(RefreshTokenRequest request) {
//        log.info("Refreshing token");
//
//        String tokenUrl = serverUrl + "/realms/" + realm + "/protocol/openid-connect/token";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//        body.add("grant_type", "refresh_token");
//        body.add("client_id", clientId);
//        body.add("client_secret", clientSecret);
//        body.add("refresh_token", request.getRefreshToken());
//
//        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);
//
//        try {
//            ResponseEntity<Map> response = restTemplate.exchange(
//                    tokenUrl,
//                    HttpMethod.POST,
//                    requestEntity,
//                    Map.class
//            );
//
//            Map<String, Object> tokenResponse = response.getBody();
//
//            if (tokenResponse == null) {
//                throw new RuntimeException("Failed to refresh token");
//            }
//
//            return AuthResponse.builder()
//                    .accessToken((String) tokenResponse.get("access_token"))
//                    .refreshToken((String) tokenResponse.get("refresh_token"))
//                    .tokenType("Bearer")
//                    .expiresIn(((Number) tokenResponse.get("expires_in")).longValue())
//                    .build();
//
//        } catch (Exception e) {
//            log.error("Token refresh failed", e);
//            throw new RuntimeException("Invalid refresh token");
//        }
//    }
//
//    /**
//     * Get current user info from JWT
//     */
//    public AuthResponse.UserInfo getCurrentUser(String userId) {
//        log.info("Getting current user info: {}", userId);
//
//        RealmResource realmResource = keycloak.realm(realm);
//        UsersResource usersResource = realmResource.users();
//
//        UserResource userResource = usersResource.get(userId);
//        UserRepresentation user = userResource.toRepresentation();
//
//        return mapToUserInfo(user);
//    }
//
//    /**
//     * Initiate forgot password flow
//     */
//    public void forgotPassword(ForgotPasswordRequest request) {
//        log.info("Forgot password request for email: {}", request.getEmail());
//
//        RealmResource realmResource = keycloak.realm(realm);
//        UsersResource usersResource = realmResource.users();
//
//        List<UserRepresentation> users = usersResource.searchByEmail(request.getEmail(), true);
//
//        if (users.isEmpty()) {
//            // Don't reveal if email exists or not for security
//            log.warn("No user found with email: {}", request.getEmail());
//            return;
//        }
//
//        UserRepresentation user = users.get(0);
//        UserResource userResource = usersResource.get(user.getId());
//
//        try {
//            // Send password reset email
//            userResource.executeActionsEmail(List.of("UPDATE_PASSWORD"));
//            log.info("Password reset email sent to: {}", request.getEmail());
//        } catch (Exception e) {
//            log.error("Failed to send password reset email", e);
//            throw new RuntimeException("Failed to send password reset email");
//        }
//    }
//
//    /**
//     * Reset password with token
//     */
//    public void resetPassword(ResetPasswordRequest request) {
//        log.info("Resetting password with token");
//
//        // Note: In a production environment, you would validate the token
//        // and extract user information from it. For simplicity, this is a basic implementation.
//        // Keycloak handles password reset through email links by default.
//
//        throw new UnsupportedOperationException(
//            "Password reset is handled through Keycloak email flow. " +
//            "Use the link sent to your email to reset your password."
//        );
//    }
//
//    // Helper methods
//
//    private void assignRole(UserResource userResource, RealmResource realmResource, String roleName) {
//        RoleRepresentation role = realmResource.roles().get(roleName).toRepresentation();
//        userResource.roles().realmLevel().add(Collections.singletonList(role));
//        log.info("Assigned role {} to user", roleName);
//    }
//
//    private UserRepresentation getUserByUsername(String username) {
//        RealmResource realmResource = keycloak.realm(realm);
//        UsersResource usersResource = realmResource.users();
//
//        List<UserRepresentation> users = usersResource.search(username, true);
//
//        if (users.isEmpty()) {
//            // Try searching by email
//            users = usersResource.searchByEmail(username, true);
//        }
//
//        if (users.isEmpty()) {
//            throw new RuntimeException("User not found");
//        }
//
//        return users.get(0);
//    }
//
//    private AuthResponse.UserInfo mapToUserInfo(UserRepresentation user) {
//        // Get user roles
//        RealmResource realmResource = keycloak.realm(realm);
//        UserResource userResource = realmResource.users().get(user.getId());
//        List<RoleRepresentation> roles = userResource.roles().realmLevel().listEffective();
//
//        String role = roles.stream()
//                .map(RoleRepresentation::getName)
//                .filter(r -> r.equals("INFLUENCER") || r.equals("BRAND") || r.equals("ADMIN"))
//                .findFirst()
//                .orElse("USER");
//
//        return AuthResponse.UserInfo.builder()
//                .id(user.getId())
//                .username(user.getUsername())
//                .email(user.getEmail())
//                .firstName(user.getFirstName())
//                .lastName(user.getLastName())
//                .role(role)
//                .emailVerified(user.isEmailVerified())
//                .build();
//    }
}
