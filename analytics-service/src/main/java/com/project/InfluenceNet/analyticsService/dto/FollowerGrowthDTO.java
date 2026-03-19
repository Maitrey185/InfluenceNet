package com.project.InfluenceNet.analyticsService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class FollowerGrowthDTO {
    private LocalDate kpiDate;
    private Integer followerCount;
}
