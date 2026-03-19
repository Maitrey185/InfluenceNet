package com.project.InfluenceNet.analyticsService.dto;

public interface OverviewProjection {
    Long getTotalPosts();
    Long getTotalLikes();
    Long getTotalComments();
    Long getTotalShares();
    Long getTotalSaves();
    Long getTotalReach();
    Long getTotalViews();

    Double getAvgLikes();
    Double getAvgComments();
    Double getAvgEngagementRate();

    Integer getFollowersStart();   // snapshot at or before startDate
    Integer getFollowersEnd();     // snapshot at or before endDate
    Integer getFollowersGained();  // end - start (may be negative if lost followers)
}
