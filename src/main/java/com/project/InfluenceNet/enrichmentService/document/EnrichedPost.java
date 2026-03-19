package com.project.InfluenceNet.enrichmentService.document;


import com.project.InfluenceNet.contracts.posts.Platform;
import com.project.InfluenceNet.contracts.posts.PostType;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
@Document(collection = "enriched_posts")
public class EnrichedPost {

    @Id
    @Field(name = "post_id")
    private String postId;

    @Field(name = "influencer_id")
    private UUID influencerId;

    private Platform platform;

    private Enrichments enrichments;

    @Field(name = "enriched_at")
    private Instant enrichedAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Enrichments {
        private List<String> hashtags;
        private List<String> mentions;
        private String language;
        private String sentiment; // "positive" | "neutral" | "negative"
        private PostType postType; // "image" | "video" | "carousel"

//        private MediaMetadata mediaMetadata;
    }

//    @Data
//    @NoArgsConstructor
//    @AllArgsConstructor
//    @Builder
//    public static class MediaMetadata {
//        private Integer durationSeconds;
//        private Integer width;
//        private Integer height;
//    }
}
