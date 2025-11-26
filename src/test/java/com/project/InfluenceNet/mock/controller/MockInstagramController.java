package com.project.InfluenceNet.mock.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/v24.0")
public class MockInstagramController {

    @GetMapping("/{userId}")
    @ResponseBody
    public Map<String, Object> getUserData(
            @PathVariable String userId,
            @RequestParam String fields,
            @RequestParam String access_token) {

        // Create a sample user response
        return Map.of(
                "id", "17841405793187218",
                "username", "test_user",
                "name", "Test User",
                "biography", "Digital Creator | Content Creator | Photographer",
                "followers_count", 12500,
                "follows_count", 342,
                "media_count", 156,
                "profile_picture_url", "https://example.com/profile.jpg",
                "website", "https://example.com"
        );
    }

    @GetMapping("/{userId}/media")
    @ResponseBody
    public Map<String, Object> getUserMedia(
            @PathVariable String userId,
            @RequestParam String fields,
            @RequestParam String access_token) {

        // Create sample media data
        var media1 = Map.of(
                "id", "17853951316123456",
                "media_type", "IMAGE",
                "media_url", "https://example.com/media1.jpg",
                "caption", "Beautiful day! #photography #nature",
                "permalink", "https://www.instagram.com/p/ABC123/",
                "username", "test_user",
                "timestamp", LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ISO_DATE_TIME),
                "like_count", 342,
                "comments_count", 28
        );

        var media2 = Map.of(
                "id", "17853951316123457",
                "media_type", "VIDEO",
                "media_url", "https://example.com/video1.mp4",
                "thumbnail_url", "https://example.com/thumbnail1.jpg",
                "caption", "Check out my new video! #video #contentcreator",
                "permalink", "https://www.instagram.com/p/DEF456/",
                "username", "test_user",
                "timestamp", LocalDateTime.now().minusDays(3).format(DateTimeFormatter.ISO_DATE_TIME),
                "like_count", 1245,
                "comments_count", 87
        );

        var response = Map.of(
                "data", List.of(media1, media2),
                "paging", Map.of(
                        "cursors", Map.of(
                                "before", "QVFIU...",
                                "after", "QVFIU..."
                        ),
                        "next", "https://graph.instagram.com/v24.0/" + userId + "/media?after=QVFIU..."
                )
        );

        return response;
    }

    @GetMapping("/{mediaId}/insights")
    @ResponseBody
    public Map<String, List<Map<String, Object>>> getInsights(
            @PathVariable String mediaId,
            @RequestParam String fields,
            @RequestParam String access_token) {

        // Create sample insights data for media
        var insightsData = List.of(
                Map.of(
                        "id", "18364894759086057/shares",
                        "name", "shares",
                        "title", "Shares",
                        "description", "Total number of times the media was shared",
                        "period", "lifetime",
                        "values", List.of(Map.of("value", 320))
                ),
                Map.of(
                        "id", "18364894759086057/comments",
                        "name", "comments",
                        "title", "Comments",
                        "description", "Total number of comments on the media",
                        "period", "lifetime",
                        "values", List.of(Map.of("value", 145))
                ),
                Map.of(
                        "id", "18364894759086057/likes",
                        "name", "likes",
                        "title", "Likes",
                        "description", "Total number of likes on the media",
                        "period", "lifetime",
                        "values", List.of(Map.of("value", 8421))
                ),
                Map.of(
                        "id", "18364894759086057/saved",
                        "name", "saved",
                        "title", "Saves",
                        "description", "Total number of saves/bookmarks on the media",
                        "period", "lifetime",
                        "values", List.of(Map.of("value", 279))
                ),
                Map.of(
                        "id", "18364894759086057/ig_reels_video_view_total_time",
                        "name", "ig_reels_video_view_total_time",
                        "title", "Total Watch Time",
                        "description", "Total time viewers spent watching the media (in seconds)",
                        "period", "lifetime",
                        "values", List.of(Map.of("value", 358920))
                ),
                Map.of(
                        "id", "18364894759086057/ig_reels_avg_watch_time",
                        "name", "ig_reels_avg_watch_time",
                        "title", "Average Watch Time",
                        "description", "Average time viewers spent watching the media (in seconds)",
                        "period", "lifetime",
                        "values", List.of(Map.of("value", 12.5))
                ),
                Map.of(
                        "id", "18364894759086057/reach",
                        "name", "reach",
                        "title", "Reach",
                        "description", "Total number of unique users who saw the media",
                        "period", "lifetime",
                        "values", List.of(Map.of("value", 12500))
                ),
                Map.of(
                        "id", "18364894759086057/impressions",
                        "name", "impressions",
                        "title", "Impressions",
                        "description", "Total number of times the media was seen",
                        "period", "lifetime",
                        "values", List.of(Map.of("value", 18750))
                ),
                Map.of(
                        "id", "18364894759086057/engagement",
                        "name", "engagement",
                        "title", "Engagement",
                        "description", "Total engagement on the media (likes + comments + shares + saves)",
                        "period", "lifetime",
                        "values", List.of(Map.of("value", 9165))
                ),
                Map.of(
                        "id", "18364894759086057/views",
                        "name", "views",
                        "title", "Video Views",
                        "description", "Total number of times the video was viewed",
                        "period", "lifetime",
                        "values", List.of(Map.of("value", 28710))
                )
        );

        var insights = Map.of("data", insightsData);

        return insights;
    }
}