package com.project.InfluenceNet.socialConnector.service;

import com.project.InfluenceNet.influencer.dto.SocialAccountResponse;
import com.project.InfluenceNet.influencer.entity.SocialAccount;
import com.project.InfluenceNet.influencer.repository.SocialAccountsRepository;
import com.project.InfluenceNet.influencer.service.SocialAccountService;
import com.project.InfluenceNet.socialConnector.documents.Platforms;
import com.project.InfluenceNet.socialConnector.dto.InstagramProfileDTO;
import com.project.InfluenceNet.socialConnector.dto.InstagramRecentPostsDTO;
import com.project.InfluenceNet.socialConnector.dto.MediaInsightsDTO;
import com.project.InfluenceNet.socialConnector.dto.MediaInsightsResponse;
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
    private final SocialAccountService socialAccountService;
    private final RawIngestionService rawIngestionService;

    public void syncInstagramProfile(SocialAccountResponse account){
        InstagramProfileDTO instagramProfileDTO = instagramConnector.fetchProfile(account.getPlatformUserId());
    }

    public void syncInstagramMedia(SocialAccountResponse account){

//            LocalDate since = calculateSinceForPosts(acc); // e.g. last sync or last 7 days
            List<InstagramRecentPostsDTO> posts = instagramConnector.fetchRecentPosts(account.getPlatformUserId(), LocalDateTime.now().minusHours(6));
            rawIngestionService.ingestRawPosts(posts, account.getInfluencerId());

    }

    public void syncInstagramInsights(SocialAccountResponse account){

            List<InstagramRecentPostsDTO> posts = instagramConnector.fetchRecentPosts(account.getPlatformUserId(), LocalDateTime.now().minusWeeks(2));
            posts.forEach(post -> {
                MediaInsightsDTO mediaInsightsResponse = instagramConnector.fetchPostInsights(post.getId());
                rawIngestionService.ingestRawInsights(post.getId(), mediaInsightsResponse, account.getInfluencerId());
            });

    }
}
