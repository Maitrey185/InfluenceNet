package com.project.InfluenceNet.analyticsService.controller;

import com.project.InfluenceNet.analyticsService.dto.OverviewDTO;
import com.project.InfluenceNet.analyticsService.dto.OverviewRequestDTO;
import com.project.InfluenceNet.analyticsService.entity.InfluencerKPI;
import com.project.InfluenceNet.analyticsService.service.InfluencerKPIService;
import com.project.InfluenceNet.socialConnector.documents.Platform;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AnalyticsDashboardController {

    private final InfluencerKPIService influencerKPIService;

    @GetMapping("/analytics/dashboard")
    public ResponseEntity<OverviewDTO> getOverview(
            @RequestParam UUID influencerId,
            @RequestParam Platform platform,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(influencerKPIService.calculateOverview(influencerId, platform, startDate, endDate));
    }
}
