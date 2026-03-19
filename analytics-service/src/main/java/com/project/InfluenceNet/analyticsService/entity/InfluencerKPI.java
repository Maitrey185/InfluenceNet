package com.project.InfluenceNet.analyticsService.entity;

import com.project.InfluenceNet.contracts.posts.Platform;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(
        name = "influencer_kpi",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"influencer_id", "platform", "kpi_date"})
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InfluencerKPI {

    @Id
    private UUID id;

    @Column(name = "influencer_id")
    private UUID influencerId;

    @Enumerated(EnumType.STRING)
    private Platform platform;

    @Column(name = "kpi_date")
    private LocalDate kpiDate;

    @Column(name = "post_count")
    private int postCount;

    @Column(name = "total_likes")
    private int totalLikes;

    @Column(name = "total_comments")
    private int totalComments;

    @Column(name = "total_shares")
    private int totalShares;

    @Column(name = "total_saves")
    private int totalSaves;

    @Column(name = "followers_count")
    private int followersCount;

    @Column(name = "avg_engagement_rate")
    private double avgEngagementRate;

    @Column(name = "total_reach")
    private int totalReach;

    @Column(name = "total_views")
    private int totalViews;

    public static InfluencerKPI newForDay(UUID influencerId, Platform platform, LocalDate kpiDate){
        return InfluencerKPI.builder()
                .id(UUID.randomUUID())
                .influencerId(influencerId)
                .platform(platform)
                .kpiDate(kpiDate)
                .postCount(0)
                .totalLikes(0)
                .totalComments(0)
                .totalShares(0)
                .totalSaves(0)
                .followersCount(0)
                .avgEngagementRate(0.0)
                .totalReach(0)
                .build();
    }
}
