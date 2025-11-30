package com.project.InfluenceNet.influencer.service;

import com.project.InfluenceNet.influencer.dto.SocialAccountRequest;
import com.project.InfluenceNet.influencer.dto.SocialAccountResponse;
import com.project.InfluenceNet.influencer.entity.InfluencerProfile;
import com.project.InfluenceNet.influencer.entity.SocialAccount;
import com.project.InfluenceNet.influencer.exception.InfluencerNotFoundException;
import com.project.InfluenceNet.influencer.exception.SocialAccountNotFoundException;
import com.project.InfluenceNet.influencer.repository.InfluencerProfileRepository;
import com.project.InfluenceNet.influencer.repository.SocialAccountsRepository;
import com.project.InfluenceNet.socialConnector.documents.Platforms;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocialAccountService {

    private final SocialAccountsRepository socialAccountsRepository;
    private final InfluencerProfileRepository influencerProfileRepository;

    public List<SocialAccountResponse> getActiveSocialAccountsForPlatform(Enum<Platforms> platform){
        List<SocialAccount> socialAccounts = socialAccountsRepository.findByPlatformAndInfluencerIsActive(platform, true);
        return socialAccounts.stream()
                .map(this::mapToSocialAccountResponse)
                .collect(Collectors.toList());

    }


    public List<SocialAccount> getAllSocialAccounts(){
        return socialAccountsRepository.findAll();
    }

    public SocialAccountResponse createSocialAccount(UUID influencerId, SocialAccountRequest socialAccountRequest){
        InfluencerProfile profile = influencerProfileRepository.findById(influencerId)
                .orElseThrow(() -> new InfluencerNotFoundException("Influencer profile not found with id: " + influencerId));

        SocialAccount socialAccount = SocialAccount.builder()
                .influencer(profile)
                .platform(socialAccountRequest.getPlatform())
                .platformUserId(socialAccountRequest.getPlatformUserId())
                .accessToken(socialAccountRequest.getAccessToken())
                .refreshToken(socialAccountRequest.getRefreshToken())
                .tokenExpiresAt(socialAccountRequest.getTokenExpiresAt())
                .followerCount(socialAccountRequest.getFollowerCount())
                .engagementRate(socialAccountRequest.getEngagementRate())
                .build();

        SocialAccount savedSocialAccount = socialAccountsRepository.save(socialAccount);


        return mapToSocialAccountResponse(savedSocialAccount);
    }

    @Transactional
    public void removeSocialAccount(UUID influencerId, Enum<Platforms> platform) {
        log.info("Removing social account for influencer: {}, platform: {}", influencerId, platform);
        SocialAccount account = socialAccountsRepository.findByInfluencerIdAndPlatform(influencerId, platform)
                .orElseThrow(() -> new SocialAccountNotFoundException("Social account not found for platform: " + platform));

        socialAccountsRepository.delete(account);
        log.info("Successfully removed social account for platform: {}", platform);

    }

    @Transactional(readOnly = true)
    public List<SocialAccountResponse> getSocialAccounts(UUID influencerId) {
        log.info("Fetching social accounts for influencer: {}", influencerId);
        List<SocialAccount> accounts = socialAccountsRepository.findByInfluencerId(influencerId);
        return accounts.stream()
                .map(this::mapToSocialAccountResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SocialAccountResponse getSocialAccount(UUID influencerId, Enum<Platforms> platform) {
        log.info("Fetching social account for influencer: {}, platform: {}", influencerId, platform);
        SocialAccount account = socialAccountsRepository.findByInfluencerIdAndPlatform(influencerId, platform)
                .orElseThrow(() -> new SocialAccountNotFoundException("Social account not found for platform: " + platform));
        return mapToSocialAccountResponse(account);
    }



    @Transactional
    public SocialAccountResponse updateSocialAccountSync(UUID influencerId, Enum<Platforms> platform, Integer followerCount) {
        log.info("Updating social account sync for influencer: {}, platform: {}", influencerId, platform);
        SocialAccount account = socialAccountsRepository.findByInfluencerIdAndPlatform(influencerId, platform)
                .orElseThrow(() -> new SocialAccountNotFoundException("Social account not found for platform: " + platform));

        account.setFollowerCount(followerCount);

        SocialAccount updatedAccount = socialAccountsRepository.save(account);
        log.info("Successfully updated social account sync");

        // Update total follower count
        updateTotalFollowerCount(influencerId);

        return mapToSocialAccountResponse(updatedAccount);
    }

    private void updateTotalFollowerCount(UUID influencerId) {
        List<SocialAccount> accounts = socialAccountsRepository.findByInfluencerId(influencerId);
        int totalFollowers = accounts.stream()
                .mapToInt(account -> account.getFollowerCount() != null ? account.getFollowerCount() : 0)
                .sum();

        InfluencerProfile profile = influencerProfileRepository.findById(influencerId)
                .orElseThrow(() -> new InfluencerNotFoundException("Influencer profile not found with id: " + influencerId));
        profile.setTotalFollowerCount(totalFollowers);
        influencerProfileRepository.save(profile);
        log.info("Updated total follower count for influencer: {} to {}", influencerId, totalFollowers);
    }

    private SocialAccountResponse mapToSocialAccountResponse(SocialAccount account) {
        return SocialAccountResponse.builder()
                .id(account.getId())
                .influencerId(account.getInfluencer().getId())
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
