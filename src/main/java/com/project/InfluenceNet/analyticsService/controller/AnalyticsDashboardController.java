package com.project.InfluenceNet.analyticsService.controller;

import com.project.InfluenceNet.analyticsService.dto.FollowerGrowthProjection;
import com.project.InfluenceNet.analyticsService.dto.OverviewDTO;
import com.project.InfluenceNet.analyticsService.dto.OverviewRequestDTO;
import com.project.InfluenceNet.analyticsService.dto.TopPostProjection;
import com.project.InfluenceNet.analyticsService.entity.InfluencerKPI;
import com.project.InfluenceNet.analyticsService.entity.PostAnalytics;
import com.project.InfluenceNet.analyticsService.service.AnalyticsService;
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
    private final AnalyticsService analyticsService;

    @GetMapping("/analytics/dashboard")
    public ResponseEntity<OverviewDTO> getOverview(
            @RequestParam UUID influencerId,
            @RequestParam Platform platform,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(influencerKPIService.calculateOverview(influencerId, platform, startDate, endDate));
    }

    @GetMapping("/analytics/timeSeriesKpi")
    public ResponseEntity<List<InfluencerKPI>> getTimeSeriesKpis(
            @RequestParam UUID influencerId,
            @RequestParam Platform platform,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(influencerKPIService.getTimeSeriesKPIData(influencerId, platform, startDate, endDate));
    }

    @GetMapping("/analytics/topPosts")
    public ResponseEntity<List<TopPostProjection>> getTopPosts(
            @RequestParam UUID influencerId,
            @RequestParam Platform platform,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam int limit) {
        return ResponseEntity.ok(analyticsService.fetchTopPost(influencerId, platform, startDate, endDate, limit));
    }

    @GetMapping("/analytics/followersGrowth")
    public ResponseEntity<List<FollowerGrowthProjection>> getFollowersGrowth(
            @RequestParam UUID influencerId,
            @RequestParam Platform platform,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(influencerKPIService.getFollowersGrowth(influencerId, platform, startDate, endDate));
    }
}
