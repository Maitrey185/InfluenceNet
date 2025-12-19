package com.project.InfluenceNet.analyticsService.dto;

public interface OverviewAggProjection {
    Long getTotalPosts();
    Long getTotalLikes();
    Long getTotalComments();
    Long getTotalShares();
    Long getTotalSaves();
    Long getTotalReach();
    Long getTotalViews();
}
