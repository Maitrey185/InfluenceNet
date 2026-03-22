package com.project.InfluenceNet.auth.controller;

import com.project.InfluenceNet.contracts.InfluencerPostContract.User;
import com.project.InfluenceNet.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;
    @GetMapping("/profile")
    public ResponseEntity<String> getProfile() {
        return ResponseEntity.ok("Check");
    }

    @GetMapping("/getByUserName/{username}")
    public ResponseEntity<User> getByUserName(@PathVariable String username){
        return ResponseEntity.ok(authService.loadUserByUsername(username));
    }

    @GetMapping("/getById/{userId}")
    public ResponseEntity<User> getById(@PathVariable UUID userId){
        return ResponseEntity.ok(authService.findById(userId));
    }
}
