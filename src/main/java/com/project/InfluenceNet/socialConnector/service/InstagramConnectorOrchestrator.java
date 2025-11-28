package com.project.InfluenceNet.socialConnector.service;

import com.project.InfluenceNet.influencer.entity.SocialAccount;
import com.project.InfluenceNet.influencer.repository.SocialAccountsRepository;
import com.project.InfluenceNet.socialConnector.dto.InstagramProfileDTO;
import com.project.InfluenceNet.socialConnector.dto.InstagramRecentPostsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InstagramConnectorOrchestrator {

    private final InstagramConnector instagramConnector;
    private final SocialAccountsRepository socialAccountRepository;
    private final RawIngestionService rawIngestionService;

    public void syncInstagramProfile(){
        List<SocialAccount> accounts = socialAccountRepository.findByPlatformAndInfluencerIsActive("Instagram", true);

        accounts.forEach(account -> {
            InstagramProfileDTO instagramProfileDTO = instagramConnector.fetchProfile(account.getPlatformUserId());

        });
    }

    public void syncInstagramMedia(){
        List<SocialAccount> accounts = socialAccountRepository.findByPlatformAndInfluencerIsActive("Instagram", true);

        accounts.forEach(acc -> {
//            LocalDate since = calculateSinceForPosts(acc); // e.g. last sync or last 7 days
            List<InstagramRecentPostsDTO> posts = instagramConnector.fetchRecentPosts(acc.getPlatformUserId(), LocalDateTime.now().minusHours(6));
            rawIngestionService.ingestRawPosts(posts, acc.getInfluencer().getId());
        });
    }

    public void syncInstagramInsights(){
        List<SocialAccount> accounts = socialAccountRepository.findByPlatformAndInfluencerIsActive("Instagram", true);

        accounts.forEach(acc -> {
            List<InstagramRecentPostsDTO> posts = instagramConnector.fetchRecentPosts(acc.getPlatformUserId(), LocalDateTime.now().minusWeeks(2));
            posts.forEach(post -> {
                instagramConnector.fetchPostInsights(post.getId());
            });
        });
    }
}
