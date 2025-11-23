package com.project.InfluenceNet.influencer.service;

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

    @Transactional
    public SocialAccountResponse addSocialAccount(UUID influencerId, SocialAccountRequest request) {
        log.info("Adding social account for influencer: {}, platform: {}", influencerId, request.getPlatform());

        InfluencerProfile profile = influencerProfileRepository.findById(influencerId)
                .orElseThrow(() -> new InfluencerNotFoundException("Influencer profile not found with id: " + influencerId));

        // Check if social account already exists for this platform
        if (socialAccountRepository.existsByInfluencerIdAndPlatform(influencerId, request.getPlatform())) {
            throw new DuplicateResourceException("Social account already exists for platform: " + request.getPlatform());
        }

        SocialAccount socialAccount = new SocialAccount();
        socialAccount.setInfluencer(profile);
        socialAccount.setPlatform(request.getPlatform());
        socialAccount.setPlatformUserId(request.getPlatformUserId());
        socialAccount.setAccessToken(request.getAccessToken());
        socialAccount.setRefreshToken(request.getRefreshToken());
        socialAccount.setTokenExpiresAt(request.getTokenExpiresAt());
        socialAccount.setFollowerCount(request.getFollowerCount() != null ? request.getFollowerCount() : 0);

        SocialAccount savedAccount = socialAccountRepository.save(socialAccount);
        log.info("Successfully added social account with id: {}", savedAccount.getId());

        // Update total follower count
        updateTotalFollowerCount(influencerId);

        return mapToSocialAccountResponse(savedAccount);
    }

    @Transactional(readOnly = true)
    public List<SocialAccountResponse> getSocialAccounts(UUID influencerId) {
        log.info("Fetching social accounts for influencer: {}", influencerId);
        List<SocialAccount> accounts = socialAccountRepository.findByInfluencerId(influencerId);
        return accounts.stream()
                .map(this::mapToSocialAccountResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SocialAccountResponse getSocialAccount(UUID influencerId, String platform) {
        log.info("Fetching social account for influencer: {}, platform: {}", influencerId, platform);
        SocialAccount account = socialAccountRepository.findByInfluencerIdAndPlatform(influencerId, platform)
                .orElseThrow(() -> new SocialAccountNotFoundException("Social account not found for platform: " + platform));
        return mapToSocialAccountResponse(account);
    }

    @Transactional
    public void removeSocialAccount(UUID influencerId, String platform) {
        log.info("Removing social account for influencer: {}, platform: {}", influencerId, platform);
        SocialAccount account = socialAccountRepository.findByInfluencerIdAndPlatform(influencerId, platform)
                .orElseThrow(() -> new SocialAccountNotFoundException("Social account not found for platform: " + platform));

        socialAccountRepository.delete(account);
        log.info("Successfully removed social account for platform: {}", platform);

        // Update total follower count
        updateTotalFollowerCount(influencerId);
    }

    @Transactional
    public SocialAccountResponse updateSocialAccountSync(UUID influencerId, String platform, Integer followerCount) {
        log.info("Updating social account sync for influencer: {}, platform: {}", influencerId, platform);
        SocialAccount account = socialAccountRepository.findByInfluencerIdAndPlatform(influencerId, platform)
                .orElseThrow(() -> new SocialAccountNotFoundException("Social account not found for platform: " + platform));

        account.setFollowerCount(followerCount);

        SocialAccount updatedAccount = socialAccountRepository.save(account);
        log.info("Successfully updated social account sync");

        // Update total follower count
        updateTotalFollowerCount(influencerId);

        return mapToSocialAccountResponse(updatedAccount);
    }

    private void updateTotalFollowerCount(UUID influencerId) {
        List<SocialAccount> accounts = socialAccountRepository.findByInfluencerId(influencerId);
        int totalFollowers = accounts.stream()
                .mapToInt(account -> account.getFollowerCount() != null ? account.getFollowerCount() : 0)
                .sum();

        InfluencerProfile profile = influencerProfileRepository.findById(influencerId)
                .orElseThrow(() -> new InfluencerNotFoundException("Influencer profile not found with id: " + influencerId));
        profile.setTotalFollowerCount(totalFollowers);
        influencerProfileRepository.save(profile);
        log.info("Updated total follower count for influencer: {} to {}", influencerId, totalFollowers);
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
