package com.project.InfluenceNet.analyticsService.dto;

import com.project.InfluenceNet.socialConnector.documents.Platform;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class OverviewRequestDTO {

    private UUID influencerId;
    private Platform platform;
    private LocalDate startDate;
    private LocalDate endDate;
}
