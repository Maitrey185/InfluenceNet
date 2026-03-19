package com.project.InfluenceNet.analyticsService.dto;

import java.time.LocalDateTime;

public interface TopPostProjection {

    String getPostId();
    String getPlatform();
    String getPostType();
    LocalDateTime getPostedAt();

    Long getLikes();
    Long getComments();
    Long getShares();
    Long getSaves();
    Long getReach();
    Long getViews();
    Double getEngagementRate();

}
