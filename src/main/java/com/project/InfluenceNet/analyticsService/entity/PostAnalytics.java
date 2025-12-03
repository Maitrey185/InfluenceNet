package com.project.InfluenceNet.analyticsService.entity;

import com.project.InfluenceNet.socialConnector.documents.Platform;
import com.project.InfluenceNet.socialConnector.documents.PostType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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

}
