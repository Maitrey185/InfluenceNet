package com.project.InfluenceNet.analyticsService.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class BestPostingTimeHeatmapResponse {

    private List<String> days;
    private List<Integer> hours;
    private long[][] matrix;
}
