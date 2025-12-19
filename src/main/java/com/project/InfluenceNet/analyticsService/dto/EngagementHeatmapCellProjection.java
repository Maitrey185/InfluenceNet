package com.project.InfluenceNet.analyticsService.dto;

public interface EngagementHeatmapCellProjection {

    Integer getDayOfWeek();  // 1=Monday ... 7=Sunday
    Integer getHourOfDay();  // 0–23
    Long getEngagement();    // summed engagement
}

