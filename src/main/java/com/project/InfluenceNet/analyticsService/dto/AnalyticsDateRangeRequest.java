package com.project.InfluenceNet.analyticsService.dto;

import com.project.InfluenceNet.socialConnector.documents.Platform;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.UUID;

public record AnalyticsDateRangeRequest(

        @NotNull
        UUID influencerId,

        @NotNull
        Platform platform,

        @NotNull
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate startDate,

        @NotNull
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate endDate
) {}
