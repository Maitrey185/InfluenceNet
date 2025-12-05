package com.project.InfluenceNet.analyticsService.entity;

import com.project.InfluenceNet.socialConnector.documents.Platform;
import com.project.InfluenceNet.socialConnector.documents.PostType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "post_analytics")
@Getter
@Setter
@Builder
public class PostAnalytics {

    @Id
    private String postId;

    @Column(name = "influencer_id")
    private UUID influencerId;

    @Enumerated(EnumType.STRING)
    private Platform platform;

    @Column(name = "post_type")
    private PostType postType;

    @Column(name = "posted_at")
    private LocalDateTime postedAt;

    private int likes;

    private int comments;

    private int shares;

    private int saves;

    private int engagementRate;

    private int reach;

    private int views;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    public static PostAnalytics newForPost(String postId, UUID influencerId, Platform platform, PostType postType, LocalDateTime postedAt){
        return PostAnalytics.builder()
                .postId(postId)
                .influencerId(influencerId)
                .platform(platform)
                .postType(postType)
                .postedAt(postedAt)
                .build();
    }

}
