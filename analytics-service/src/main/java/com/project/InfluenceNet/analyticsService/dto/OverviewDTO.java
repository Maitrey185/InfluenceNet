package com.project.InfluenceNet.analyticsService.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class OverviewDTO {

    private Long totalPosts;
    private Long totalLikes;
    private Long totalComments;
    private Long totalShares;
    private Long totalSaves;
    private Long totalReach;
    private Long totalViews;
    private Double avgEngagementRate;
    private Long followersGained;
}
