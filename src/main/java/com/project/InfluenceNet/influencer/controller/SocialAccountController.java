package com.project.InfluenceNet.influencer.controller;

import com.project.InfluenceNet.influencer.dto.SocialAccountRequest;
import com.project.InfluenceNet.influencer.dto.SocialAccountResponse;
import com.project.InfluenceNet.influencer.entity.SocialAccount;
import com.project.InfluenceNet.influencer.service.SocialAccountService;
import com.project.InfluenceNet.contracts.posts.Platform;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/socialAccount")
@RequiredArgsConstructor
public class SocialAccountController {

    private final SocialAccountService socialAccountService;

    @PostMapping("/{influencerId}")
    public ResponseEntity<SocialAccountResponse> createSocialAccount(@PathVariable UUID influencerId, @RequestBody SocialAccountRequest request) throws Throwable {
        return ResponseEntity.ok(socialAccountService.createSocialAccount(influencerId, request));
    }

    @GetMapping
    public ResponseEntity<List<SocialAccount>> getAllSocialAccounts() {
        return ResponseEntity.ok(socialAccountService.getAllSocialAccounts());
    }

    @DeleteMapping("/{influencerId}/{platform}")
    public ResponseEntity<Void> removeSocialAccount(@PathVariable UUID influencerId, @PathVariable Enum<Platform> platform) {
        socialAccountService.removeSocialAccount(influencerId, platform);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{influencerId}")
    public ResponseEntity<List<SocialAccountResponse>> getSocialAccounts(@PathVariable UUID influencerId) {
        return ResponseEntity.ok(socialAccountService.getSocialAccounts(influencerId));
    }

    @GetMapping("/{influencerId}/{platform}")
    public ResponseEntity<SocialAccountResponse> getSocialAccount(@PathVariable UUID influencerId, @PathVariable Enum<Platform> platform) {
        return ResponseEntity.ok(socialAccountService.getSocialAccount(influencerId, platform));
    }



}
