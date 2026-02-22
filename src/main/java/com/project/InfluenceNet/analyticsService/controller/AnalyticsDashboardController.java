package com.project.InfluenceNet.analyticsService.controller;

import com.project.InfluenceNet.analyticsService.dto.*;
import com.project.InfluenceNet.analyticsService.entity.InfluencerKPI;
import com.project.InfluenceNet.analyticsService.entity.PostAnalytics;
import com.project.InfluenceNet.analyticsService.service.AnalyticsService;
import com.project.InfluenceNet.analyticsService.service.EngagementHeatmapService;
import com.project.InfluenceNet.analyticsService.service.InfluencerKPIService;
import com.project.InfluenceNet.socialConnector.documents.Platform;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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

    private final EngagementHeatmapService engagementHeatmapService;

    @GetMapping("/analytics/dashboard")
    public ResponseEntity<OverviewDTO> getOverview(
            @Valid AnalyticsDateRangeRequest request) {
        return ResponseEntity.ok(influencerKPIService.calculateOverview(request.influencerId(), request.platform(), request.startDate(), request.endDate()));
    }

    @GetMapping("/analytics/timeSeriesKpi")
    public ResponseEntity<List<InfluencerKPI>> getTimeSeriesKpis(
            @Valid AnalyticsDateRangeRequest request) {
        return ResponseEntity.ok(influencerKPIService.getTimeSeriesKPIData(request.influencerId(), request.platform(), request.startDate(), request.endDate()));
    }

    @GetMapping("/analytics/topPosts")
    public ResponseEntity<List<TopPostProjection>> getTopPosts(
            @Valid AnalyticsDateRangeRequest request,
            @RequestParam int limit) {
        return ResponseEntity.ok(analyticsService.fetchTopPost(request.influencerId(), request.platform(), request.startDate(), request.endDate(), limit));
    }

    @GetMapping("/analytics/followersGrowth")
    public ResponseEntity<List<FollowerGrowthDTO>> getFollowersGrowth(@Valid AnalyticsDateRangeRequest request) {
        return ResponseEntity.ok(influencerKPIService.getFollowersGrowth(request.influencerId(), request.platform(), request.startDate(), request.endDate()));
    }

    @GetMapping("/analytics/engagementHeatmapBestTimeToPost")
    public ResponseEntity<BestPostingTimeHeatmapResponse> getEngagementHeatmapBestTimeToPost(@Valid AnalyticsDateRangeRequest request) {
        return ResponseEntity.ok(engagementHeatmapService.getBestPostingTimeHeatmap(request.influencerId(), request.platform(), request.startDate(), request.endDate()));
    }
}
