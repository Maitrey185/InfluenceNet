package com.project.InfluenceNet.auth.service;

import com.project.InfluenceNet.auth.dto.*;
//import jakarta.ws.rs.core.Response;
import com.project.InfluenceNet.auth.entity.User;
import com.project.InfluenceNet.auth.repository.UserRepository;
import com.project.InfluenceNet.auth.utils.JwtUtil;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Loading user by username={}", username);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("User not found for username={}", username);
                    return new UsernameNotFoundException("User not found");
                });
    }

    public User registerUser(RegisterRequest registerRequest) {
        log.info("Registering new user username={} email={} role={}", registerRequest.getUsername(), registerRequest.getEmail(), registerRequest.getRole());
        userRepository.findByUsername(registerRequest.getUsername())
                .ifPresent(user -> {
                    log.warn("Registration rejected: user already exists username={}", registerRequest.getUsername());
                    throw new IllegalArgumentException("User already exists");
                });

        User user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password_hash(registerRequest.getPassword())
                .role(registerRequest.getRole())
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        userRepository.save(user);
        log.info("User registration persisted username={} userId={}", user.getUsername(), user.getId());
        return user;
    }


//    public String loginUser(LoginRequest loginRequest) {
//
//    }
}
