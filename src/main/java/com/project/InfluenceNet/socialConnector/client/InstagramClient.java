package com.project.InfluenceNet.socialConnector.client;

import com.project.InfluenceNet.socialConnector.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class InstagramClient {

    private final WebClient webClient;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public InstagramClient(@Value("${instagram.api.base-url}") String baseUrl) {
        System.out.println("Instagram base URL = {}"+baseUrl);
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    public InstagramProfileDTO getInstagramUserData(String platformUserId) {
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
                        .path("/v24.0/{userId}")
                        .queryParam("fields", fields)
                        .queryParam("access_token", "dummy")
                        .build("dummy")
                )
                .retrieve()
                .bodyToMono(InstagramProfileDTO.class)
                .block();

    }

    public List<InstagramRecentPostsDTO> getInstagramMediaData(String platformUserId, LocalDateTime since) {
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

        Map<String, Object> response =  webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v24.0/{userId}/media")
                        .queryParam("fields", fields)
                        .queryParam("access_token", "dummy")
                        .build("dummy")
                )
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();


        return mapToInstagramRecentPostsDTO((List<Map<String, Object>>)response.get("data"));
    }

    public List<InstagramRecentPostsDTO> mapToInstagramRecentPostsDTO(List<Map<String, Object>> response) {
        List<InstagramRecentPostsDTO> recentPosts = response.stream()
                .map(post -> InstagramRecentPostsDTO.builder()
                        .id((String) post.get("id"))
                        .media_type((String) post.get("media_type"))
                        .media_url((String) post.get("media_url"))
                        .thumbnail_url((String) post.get("thumbnail_url"))
                        .caption((String) post.get("caption"))
                        .permalink((String) post.get("permalink"))
                        .username((String) post.get("username"))
                        .timestamp((String) post.get("timestamp"))
                        .like_count((Integer) post.get("like_count"))
                        .comments_count((Integer) post.get("comments_count"))
                        .build())
                .toList();

        return recentPosts;

    }

    public MediaInsightsDTO getInstagramInsightsData(String mediaId, String accessToken) {
        try {
            String fields = String.join(",",
                    "likes", "comments", "shares", "saves", "reach",
                    "impressions", "ig_reels_video_view_total_time",
                    "ig_reels_avg_watch_time", "total_interactions", "views"
            );

            Map<String, Object> response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v24.0/{mediaId}/insights")
                            .queryParam("fields", fields)
                            .queryParam("access_token", accessToken)
                            .build(mediaId)
                    )
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                            clientResponse -> clientResponse.bodyToMono(String.class)
                                    .flatMap(errorBody -> {
                                        log.error("Error fetching Instagram insights: {}", errorBody);
                                        return Mono.error(new RuntimeException("Failed to fetch Instagram insights: " + errorBody));
                                    }))
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();

            log.info("Instagram insights API response: {}", response);
            return mapToMediaInsightsDTO(mediaId, response);
//            return mapToMediaInsightsResponse(response);
        } catch (Exception e) {
            log.error("Exception while fetching Instagram insights", e);
            throw new RuntimeException("Failed to fetch Instagram insights", e);
        }
    }

    private MediaInsightsResponse mapToMediaInsightsResponse(Map<String, Object> response) {
        if (response == null || !response.containsKey("data")) {
            log.warn("No data found in Instagram insights response");
            return MediaInsightsResponse.builder().build();
        }

        List<Map<String, Object>> insightsData = (List<Map<String, Object>>) response.get("data");
        List<MediaInsights> mediaInsightsList = new ArrayList<>();

        for (Map<String, Object> insight : insightsData) {
            MediaInsights mediaInsight = MediaInsights.builder()
                    .name((String) insight.get("name"))
                    .period((String) insight.get("period"))
                    .title((String) insight.get("title"))
                    .description((String) insight.get("description"))
                    .values(extractValues(insight.get("values")))
                    .build();
            mediaInsightsList.add(mediaInsight);
        }

        return MediaInsightsResponse.builder()
                .mediaInsights(mediaInsightsList)
                .build();
    }

    @SuppressWarnings("unchecked")
    private List<InsightValue> extractValues(Object values) {
        return (List<InsightValue>) values;
    }

    private MediaInsightsDTO mapToMediaInsightsDTO(String mediaId, Map<String, Object> response) {
        List<Map<String, Object>> insightsData = (List<Map<String, Object>>) response.get("data");

        return MediaInsightsDTO.builder()
                .id(mediaId)
                .shares((Integer) ((List<Map<String, Object>>) insightsData.get(0).get("values")).get(0).get("value"))
                .comments((Integer) ((List<Map<String, Object>>) insightsData.get(1).get("values")).get(0).get("value"))
                .likes((Integer) ((List<Map<String, Object>>) insightsData.get(2).get("values")).get(0).get("value"))
                .saved((Integer) ((List<Map<String, Object>>) insightsData.get(3).get("values")).get(0).get("value"))
                .ig_reels_video_view_total_time((Integer) ((List<Map<String, Object>>) insightsData.get(4).get("values")).get(0).get("value"))
                .ig_reels_avg_watch_time((Double) ((List<Map<String, Object>>) insightsData.get(5).get("values")).get(0).get("value"))
                .reach((Integer) ((List<Map<String, Object>>) insightsData.get(6).get("values")).get(0).get("value"))
                .impressions((Integer) ((List<Map<String, Object>>) insightsData.get(7).get("values")).get(0).get("value"))
                .engagement((Integer) ((List<Map<String, Object>>) insightsData.get(8).get("values")).get(0).get("value"))
                .views((Integer) ((List<Map<String, Object>>) insightsData.get(9).get("values")).get(0).get("value"))
                .build();
    }



}
