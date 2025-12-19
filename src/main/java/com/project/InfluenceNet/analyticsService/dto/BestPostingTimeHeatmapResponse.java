package com.project.InfluenceNet.analyticsService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BestPostingTimeHeatmapResponse {

    private List<String> days;
    private List<Integer> hours;
    private long[][] matrix;
}
