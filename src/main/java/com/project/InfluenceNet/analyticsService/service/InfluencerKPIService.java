package com.project.InfluenceNet.analyticsService.service;

import com.project.InfluenceNet.analyticsService.dto.OverviewAggProjection;
import com.project.InfluenceNet.analyticsService.dto.OverviewDTO;
import com.project.InfluenceNet.analyticsService.entity.InfluencerKPI;
import com.project.InfluenceNet.analyticsService.repository.InfluencerKPIRepository;
import com.project.InfluenceNet.influencer.dto.InfluencerProfileResponse;
import com.project.InfluenceNet.influencer.entity.InfluencerProfile;
import com.project.InfluenceNet.influencer.exception.InfluencerNotFoundException;
import com.project.InfluenceNet.influencer.service.InfluencerProfileService;
import com.project.InfluenceNet.socialConnector.documents.Platform;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.ast.tree.expression.Over;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static java.lang.Long.getLong;

@Service
@RequiredArgsConstructor
public class InfluencerKPIService {

    private final InfluencerKPIRepository influencerKPIRepository;
    private final InfluencerProfileService influencerProfileService;

    @Transactional
    public void calculateAndStoreKPIs(int newPost, UUID influencerId, Platform platform, LocalDate fetchedAt, int likesDiff, int commentsDiff, int sharesDiff, int savesDiff, int reachDiff, int viewsDiff) {
        // Use the provided fetchedAt date instead of LocalDate.now()
        LocalDate kpiDate = fetchedAt != null ? fetchedAt : LocalDate.now();
        InfluencerProfileResponse influencerProfile;

        try{
            influencerProfile = influencerProfileService.getProfile(influencerId);
        }
        catch(Throwable e) {
            throw new RuntimeException(e);
        }
        // Get the influencer profile to update follower count

        // Find existing KPI or create a new one
        InfluencerKPI influencerKPI = influencerKPIRepository.findByInfluencerIdAndPlatformAndKpiDate(influencerId, platform, kpiDate)
                .orElseGet(() -> InfluencerKPI.newForDay(influencerId, platform, kpiDate));

        double avgEngagementRate = calculateEngagementRate(influencerKPI.getTotalLikes() + likesDiff, influencerKPI.getTotalComments()+commentsDiff , influencerKPI.getTotalShares()+sharesDiff, influencerKPI.getTotalSaves()+savesDiff, influencerProfile.getTotalFollowerCount());
        // Update KPI metrics
        influencerKPI.setPostCount(influencerKPI.getPostCount() + newPost);
        influencerKPI.setTotalLikes(influencerKPI.getTotalLikes() + likesDiff);
        influencerKPI.setTotalComments(influencerKPI.getTotalComments() + commentsDiff);
        influencerKPI.setTotalShares(influencerKPI.getTotalShares() + sharesDiff);
        influencerKPI.setTotalSaves(influencerKPI.getTotalSaves() + savesDiff);
        influencerKPI.setTotalReach(influencerKPI.getTotalReach() + reachDiff);
        influencerKPI.setTotalViews(influencerKPI.getTotalViews() + viewsDiff);
        influencerKPI.setAvgEngagementRate(avgEngagementRate);
        influencerKPI.setPlatform(platform);
        
        // Update follower count from the profile
        influencerKPI.setFollowersCount(influencerProfile.getTotalFollowerCount());

        // Save the updated KPI
        influencerKPIRepository.save(influencerKPI);
    }

    private double calculateEngagementRate(int likes, int comments, int shares, int saves, int followers) {
        return (double) (likes + comments + shares + saves) / followers;
    }

    public OverviewDTO calculateOverview(UUID influencerId,
                                         Platform platform,
                                         LocalDate startDate,
                                         LocalDate endDate) {

        OverviewAggProjection agg = influencerKPIRepository.aggregateWindow(influencerId, platform, startDate, endDate);

        Long followersBefore = influencerKPIRepository.findLatestFollowersBeforeOrOnDate(influencerId, platform.name(), startDate);
        Long followersAfter = influencerKPIRepository.findLatestFollowersBeforeOrOnDate(influencerId, platform.name(), endDate);
        if(followersBefore == null) {
            followersBefore = 0L;
        }
        if(followersAfter == null) {
            followersAfter = 0L;
        }
        Long followersGained = followersAfter - followersBefore;
        double avgEngagementRate = calculateEngagementRate(agg.getTotalLikes().intValue(),agg.getTotalComments().intValue(),agg.getTotalSaves().intValue(),agg.getTotalSaves().intValue(),followersGained.intValue());
        OverviewDTO dto = OverviewDTO.builder()
                .totalPosts(agg.getTotalPosts())
                .totalLikes(agg.getTotalLikes())
                .totalComments(agg.getTotalComments())
                .totalShares(agg.getTotalShares())
                .totalSaves(agg.getTotalSaves())
                .totalReach(agg.getTotalReach())
                .totalViews(agg.getTotalViews())
                .followersGained(followersGained)
                .avgEngagementRate(avgEngagementRate)
                .build();
        return dto;
    }

    private BigDecimal getBigDecimal(Object o) {
        if (o == null) return BigDecimal.ZERO;
        if (o instanceof BigDecimal bd) return bd;
        if (o instanceof Number n) return BigDecimal.valueOf(n.doubleValue());
        throw new IllegalArgumentException("Expected numeric, got " + o.getClass());
    }

    public List<InfluencerKPI> getTimeSeriesKPIData(UUID influencerId,
                                                    Platform platform,
                                                    LocalDate startDate,
                                                    LocalDate endDate){
        return influencerKPIRepository.findByInfluencerIdAndPlatformAndKpiDateBetween(influencerId, platform, startDate, endDate);
    }

}
