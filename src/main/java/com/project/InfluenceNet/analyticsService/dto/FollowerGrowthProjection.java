package com.project.InfluenceNet.analyticsService.dto;

import java.time.LocalDate;

public interface FollowerGrowthProjection {

    Integer getFollowerCount();
    LocalDate getKpiDate();
}
