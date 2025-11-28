package com.project.InfluenceNet.socialConnector.service;

import com.project.InfluenceNet.socialConnector.client.InstagramClient;
import com.project.InfluenceNet.socialConnector.dto.InstagramProfileDTO;
import com.project.InfluenceNet.socialConnector.dto.InstagramRecentPostsDTO;
import com.project.InfluenceNet.socialConnector.dto.MediaInsightsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InstagramConnector implements SocialConnector {

    private final InstagramClient apiClient;
//    private final TokenManager tokenManager;

    @Override
    public List<InstagramRecentPostsDTO> fetchRecentPosts(String platformUserId, LocalDateTime since) {
        return apiClient.getInstagramMediaData(platformUserId, since);
    }

    @Override
    public MediaInsightsResponse fetchPostInsights(String postId) {
        return apiClient.getInstagramInsightsData(postId, "dummy");
    }

    @Override
    public InstagramProfileDTO fetchProfile(String platformUserId) {
        return apiClient.getInstagramUserData(platformUserId);
    }

//    @Override
//    public void refreshToken(String influencerId) {
//        tokenManager.refreshInstagramToken(influencerId);
//    }
}
