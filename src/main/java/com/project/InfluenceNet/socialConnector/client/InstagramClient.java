package com.project.InfluenceNet.socialConnector.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class InstagramClient {

    @Autowired
    private WebClient webClient;

    public Map<String, Object> getInstagramUserData() {
        String fields = String.join(",",
                "biography",
                "followers_count",
                "follows_count",
                "id",
                "media_count",
                "name",
                "profile_picture_url",
                "username",
                "website"
        );

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
//                        .host("graph.instagram.com")
                        .host("localhost:8081")
                        .path("/v24.0/{userId}")
                        .queryParam("fields", fields)
                        .queryParam("access_token", "dummy")
                        .build("dummy")
                )
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();

    }

    public Map<String, Object> getInstagramMediaData() {
        String fields = String.join(",",

                "id",
                "media_type",
                "media_url",
                "thumbnail_url",
                "caption",
                "permalink",
                "username",
                "timestamp",
                "like_count",
                "comments_count"
        );

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
//                        .host("graph.instagram.com")
                        .host("localhost:8081")
                        .path("/v24.0/{userId}/media")
                        .queryParam("fields", fields)
                        .queryParam("access_token", "dummy")
                        .build("dummy")
                )
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }

    public Map<String, Object> getInstagramInsightsData() {
        String fields = String.join(",",
                "likes",
                "comments",
                "shares",
                "saves",
                "reach",
                "impressions",
                "ig_reels_video_view_total_time",
                "ig_reels_avg_watch_time",
                "total_interactions",
                "views"
        );

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
//                        .host("graph.instagram.com")
                        .host("localhost:8081")
                        .path("/v24.0/{mediaId}/insights")
                        .queryParam("fields", fields)
                        .queryParam("access_token", "dummy")
                        .build("dummy")
                )
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }



}
