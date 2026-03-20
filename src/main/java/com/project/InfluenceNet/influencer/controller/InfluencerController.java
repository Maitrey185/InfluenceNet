package com.project.InfluenceNet.influencer.controller;


import com.project.InfluenceNet.influencer.dto.InfluencerProfileRequest;
import com.project.InfluenceNet.contracts.InfluencerPostContract.InfluencerProfileResponse;
import com.project.InfluenceNet.influencer.service.InfluencerProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/influencer")
@RequiredArgsConstructor
public class InfluencerController {

    private final InfluencerProfileService influencerProfileService;

    @GetMapping("/profile/{id}")
    public ResponseEntity<InfluencerProfileResponse> getProfile(@PathVariable UUID id) throws Throwable {
        return ResponseEntity.ok(influencerProfileService.getProfile(id));
    }

    @GetMapping("/followerCount/{id}")
    public ResponseEntity<Integer> getFollowerCount(@PathVariable UUID id){
        return ResponseEntity.ok(influencerProfileService.getFollowerCount(id));
    }

    @PostMapping("/profile")
    public ResponseEntity<InfluencerProfileResponse> createProfile(@RequestBody InfluencerProfileRequest request) throws Throwable {
        return ResponseEntity.ok(influencerProfileService.createProfile(request));
    }

    @GetMapping("/getAllprofiles")
    public ResponseEntity<List<InfluencerProfileResponse>> getAllProfiles() throws Throwable {
        return ResponseEntity.ok(influencerProfileService.getAllProfiles());
    }

    @GetMapping("/email/{id}")
    public ResponseEntity<String> getEmailById(@PathVariable UUID id) throws Throwable {

        return ResponseEntity.ok(influencerProfileService.getEmailById(id));
    }

    @PutMapping("/updateFollowerCount/{id}")
    public ResponseEntity<InfluencerProfileResponse> updateFollowerCount(@PathVariable UUID id, @RequestBody Integer count) throws Throwable {
        return ResponseEntity.ok(influencerProfileService.updateFollowerCount(id, count));
    }


}
