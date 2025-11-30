package com.project.InfluenceNet.influencer.service;

import com.project.InfluenceNet.auth.entity.User;
import com.project.InfluenceNet.auth.repository.UserRepository;
import com.project.InfluenceNet.influencer.dto.InfluencerProfileRequest;
import com.project.InfluenceNet.influencer.dto.InfluencerProfileResponse;
import com.project.InfluenceNet.influencer.dto.SocialAccountRequest;
import com.project.InfluenceNet.influencer.dto.SocialAccountResponse;
import com.project.InfluenceNet.influencer.entity.InfluencerProfile;
import com.project.InfluenceNet.influencer.entity.SocialAccount;
import com.project.InfluenceNet.influencer.exception.DuplicateResourceException;
import com.project.InfluenceNet.influencer.exception.InfluencerNotFoundException;
import com.project.InfluenceNet.influencer.exception.SocialAccountNotFoundException;
import com.project.InfluenceNet.influencer.repository.InfluencerProfileRepository;
import com.project.InfluenceNet.influencer.repository.SocialAccountsRepository;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InfluencerProfileService {

    private final InfluencerProfileRepository influencerProfileRepository;
    private final SocialAccountsRepository socialAccountRepository;
    private final UserRepository userRepository;

    @Transactional
    public InfluencerProfileResponse createProfile(InfluencerProfileRequest request) {
        log.info("Creating influencer profile for email: {}", request.getEmail());

        // Check if email or username already exists
        if (influencerProfileRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists: " + request.getEmail());
        }
        if (influencerProfileRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username already exists: " + request.getUsername());
        }

        InfluencerProfile profile = new InfluencerProfile();

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new InfluencerNotFoundException("User not found with id: " + request.getUserId()));
        profile.setUser(user);
        profile.setEmail(request.getEmail());
        profile.setUsername(request.getUsername());
        profile.setCreatedAt(LocalDateTime.now());

        InfluencerProfile savedProfile = influencerProfileRepository.save(profile);
        log.info("Successfully created influencer profile with id: {}", savedProfile.getId());

        return mapToResponse(savedProfile);
    }

    @Transactional(readOnly = true)
    public InfluencerProfileResponse getProfile(UUID id) throws Throwable {
        log.info("Fetching influencer profile with id: {}", id);
        InfluencerProfile profile = influencerProfileRepository.findById(id)
                .orElseThrow(() -> new InfluencerNotFoundException("Influencer profile not found with id: " + id));
        return mapToResponse(profile);
    }

    @Transactional(readOnly = true)
    public InfluencerProfileResponse getProfileByUsername(String username) {
        log.info("Fetching influencer profile with username: {}", username);
        InfluencerProfile profile = influencerProfileRepository.findByUsername(username)
                .orElseThrow(() -> new InfluencerNotFoundException("Influencer profile not found with username: " + username));
        return mapToResponse(profile);
    }

    @Transactional(readOnly = true)
    public List<InfluencerProfileResponse> getAllProfiles() {
        log.info("Fetching all influencer profiles");
        return influencerProfileRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public InfluencerProfileResponse updateProfile(UUID id, InfluencerProfileRequest request) {
        log.info("Updating influencer profile with id: {}", id);
        InfluencerProfile profile = influencerProfileRepository.findById(id)
                .orElseThrow(() -> new InfluencerNotFoundException("Influencer profile not found with id: " + id));

        // Check if username is being changed and if it already exists
        if (!profile.getUsername().equals(request.getUsername()) &&
                influencerProfileRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username already exists: " + request.getUsername());
        }

        profile.setUsername(request.getUsername());
        profile.setEmail(request.getEmail());
        profile.setCreatedAt(LocalDateTime.now());

        InfluencerProfile updatedProfile = influencerProfileRepository.save(profile);
        log.info("Successfully updated influencer profile with id: {}", id);

        return mapToResponse(updatedProfile);
    }

    @Transactional
    public void deleteProfile(UUID id) {
        log.info("Deleting influencer profile with id: {}", id);
        if (!influencerProfileRepository.existsById(id)) {
            throw new InfluencerNotFoundException("Influencer profile not found with id: " + id);
        }
        influencerProfileRepository.deleteById(id);
        log.info("Successfully deleted influencer profile with id: {}", id);
    }




    private InfluencerProfileResponse mapToResponse(InfluencerProfile profile) {
        List<SocialAccountResponse> socialAccountResponses = profile.getSocialAccounts() != null ?
                profile.getSocialAccounts().stream()
                        .map(this::mapToSocialAccountResponse)
                        .collect(Collectors.toList()) : List.of();

        return InfluencerProfileResponse.builder()
                .id(profile.getId())
                .email(profile.getEmail())
                .username(profile.getUsername())
                .avgEngagementRate(profile.getAvgEngagementRate())
                .isActive(profile.getIsActive())
                .socialAccounts(socialAccountResponses)
                .createdAt(profile.getCreatedAt())
                .build();
    }

    private SocialAccountResponse mapToSocialAccountResponse(SocialAccount account) {
        return SocialAccountResponse.builder()
                .id(account.getId())
                .platform(account.getPlatform())
                .platformUserId(account.getPlatformUserId())
                .followerCount(account.getFollowerCount())
                .engagementRate(account.getEngagementRate())
                .accessToken(account.getAccessToken())
                .refreshToken(account.getRefreshToken())
                .tokenExpiresAt(account.getTokenExpiresAt())
                .build();
    }
}
