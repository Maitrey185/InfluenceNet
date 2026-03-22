package com.project.InfluenceNet.socialConnector.service;

import com.project.InfluenceNet.contracts.InfluencerPostContract.SocialAccountResponse;
import com.project.InfluenceNet.socialConnector.client.InfluencerServiceClient;
import com.project.InfluenceNet.socialConnector.dto.InstagramProfileDTO;
import com.project.InfluenceNet.socialConnector.dto.InstagramRecentPostsDTO;
import com.project.InfluenceNet.socialConnector.dto.MediaInsightsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InstagramConnectorOrchestrator {

    private final InstagramConnector instagramConnector;
    private final RawIngestionService rawIngestionService;
    private final InfluencerServiceClient influencerServiceClient;

    public void syncInstagramProfile(SocialAccountResponse account){
        InstagramProfileDTO instagramProfileDTO = instagramConnector.fetchProfile(account.getPlatformUserId());
        influencerServiceClient.updateFollowerCount(account.getInfluencerId(), instagramProfileDTO.getFollowersCount());
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
