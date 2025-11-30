package com.project.InfluenceNet.socialConnector.service;

import com.project.InfluenceNet.socialConnector.dto.InstagramProfileDTO;
import com.project.InfluenceNet.socialConnector.dto.InstagramRecentPostsDTO;
import com.project.InfluenceNet.socialConnector.dto.MediaInsightsDTO;
import com.project.InfluenceNet.socialConnector.dto.MediaInsightsResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface SocialConnector {

    // 1. User / Profile details
    InstagramProfileDTO fetchProfile(String platformUserId);


    List<InstagramRecentPostsDTO> fetchRecentPosts(String platformUserId, LocalDateTime since);

    // 3. Insights for specific posts
    MediaInsightsDTO fetchPostInsights(String postId);

    // Token management
//    void refreshToken(String influencerId);
}
