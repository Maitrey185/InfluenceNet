package com.project.InfluenceNet.analyticsService.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.InfluenceNet.analyticsService.dto.FollowerGrowthDTO;
import com.project.InfluenceNet.analyticsService.dto.FollowerGrowthProjection;
import com.project.InfluenceNet.analyticsService.dto.OverviewAggProjection;
import com.project.InfluenceNet.analyticsService.dto.OverviewDTO;
import com.project.InfluenceNet.analyticsService.entity.InfluencerKPI;
import com.project.InfluenceNet.analyticsService.exception.AnalyticsDataNotFoundException;
import com.project.InfluenceNet.analyticsService.repository.InfluencerKPIRepository;
import com.project.InfluenceNet.analyticsService.exception.InfluencerNotFoundException;
import com.project.InfluenceNet.contracts.InfluencerPostContract.Platform;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class InfluencerKPIService {

    private final InfluencerKPIRepository influencerKPIRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private static final Duration CACHE_TTL = Duration.ofHours(24);


    private final RedisTemplate<String, Object> redisTemplate;

    @Transactional
    public void calculateAndStoreKPIs(int newPost, UUID influencerId, Platform platform, LocalDate fetchedAt, int likesDiff, int commentsDiff, int sharesDiff, int savesDiff, int reachDiff, int viewsDiff) throws InfluencerNotFoundException {
        // Use the provided fetchedAt date instead of LocalDate.now()
        LocalDate kpiDate = fetchedAt != null ? fetchedAt : LocalDate.now();

        String influencerServiceUrl = "http://localhost:8080/api/influencer/followerCount/" + influencerId;
        ResponseEntity<Integer> followerCountResponse = null;
        try {
            followerCountResponse =
                    restTemplate.getForEntity(influencerServiceUrl, Integer.class);

            System.out.println("Response: " + followerCountResponse.getBody());

        } catch (Exception e) {
            e.printStackTrace(); // VERY IMPORTANT
        }        // Get the influencer profile to update follower count
        Integer followerCount = followerCountResponse.getBody();
        if (followerCount == null) {
            throw new InfluencerNotFoundException("Follower count not found for influencer: " + influencerId);
        }

        // Find existing KPI or create a new one
        InfluencerKPI influencerKPI = influencerKPIRepository.findByInfluencerIdAndPlatformAndKpiDate(influencerId, platform, kpiDate)
                .orElseGet(() -> InfluencerKPI.newForDay(influencerId, platform, kpiDate));

        double avgEngagementRate = calculateEngagementRate(influencerKPI.getTotalLikes() + likesDiff, influencerKPI.getTotalComments()+commentsDiff , influencerKPI.getTotalShares()+sharesDiff, influencerKPI.getTotalSaves()+savesDiff, followerCount);
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
        influencerKPI.setFollowersCount(followerCount);

        // Save the updated KPI
        influencerKPIRepository.save(influencerKPI);
    }

    private double calculateEngagementRate(int likes, int comments, int shares, int saves, int followers) {
        if(followers == 0) {
            return 0;
        }
        return (double) (likes + comments + shares + saves) / followers;
    }

    public OverviewDTO calculateOverview(UUID influencerId,
                                         Platform platform,
                                         LocalDate startDate,
                                         LocalDate endDate) {

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        String cacheKey = buildCacheKey(influencerId, platform, startDate, endDate, "overview");

        try{
            Object overviewDTO = redisTemplate.opsForValue().get(cacheKey);
            if(overviewDTO!=null){ // cache hit
                return objectMapper.convertValue(overviewDTO, OverviewDTO.class);
            }
        }catch (Exception e){
            log.warn("Redis unavailable, fallback to DB");
        }

        OverviewAggProjection agg = influencerKPIRepository.aggregateWindow(influencerId, platform, startDate, endDate);

        if (agg == null) {
            throw new AnalyticsDataNotFoundException("No analytics data found for influencerId: " + influencerId + " and platform: " + platform.name());
        }

        FollowerGrowthProjection followersB = influencerKPIRepository.findLatestFollowersBeforeOrOnDate(influencerId, platform.name(), startDate);
        FollowerGrowthProjection followersA= influencerKPIRepository.findLatestFollowersBeforeOrOnDate(influencerId, platform.name(), endDate);

        if (followersB == null||followersA==null) {
            throw new AnalyticsDataNotFoundException("No. of followers data not found for influencerId: " + influencerId + " and platform: " + platform.name());
        }

        long followersBefore = followersB.getFollowerCount();
        long followersAfter = followersA.getFollowerCount();
        if(followersBefore == 0) {
            followersBefore = 0L;
        }
        if(followersAfter == 0) {
            followersAfter = 0L;
        }
        Long followersGained = followersAfter - followersBefore;
        double avgEngagementRate = calculateEngagementRate(agg.getTotalLikes().intValue(),agg.getTotalComments().intValue(),agg.getTotalShares().intValue(),agg.getTotalSaves().intValue(),followersGained.intValue());
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

        redisTemplate.opsForValue().set(cacheKey, dto, CACHE_TTL);
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
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        return influencerKPIRepository.findByInfluencerIdAndPlatformAndKpiDateBetween(influencerId, platform, startDate, endDate);
    }

    public List<FollowerGrowthDTO> getFollowersGrowth(UUID influencerId,
                                                             Platform platform,
                                                             LocalDate startDate,
                                                             LocalDate endDate){

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        String cacheKey = buildCacheKey(influencerId, platform, startDate, endDate, "followersGrowth");

        List<FollowerGrowthDTO> cachedData = (List<FollowerGrowthDTO>) redisTemplate.opsForValue().get(cacheKey);
        if(cachedData!=null){
            return cachedData;
        }
        List<FollowerGrowthProjection> fg = influencerKPIRepository.findFollowersGrowth(influencerId, platform.name(), startDate, endDate);

        List<FollowerGrowthDTO> followerGrowthDTOS = fg.stream()
                        .map(fge -> FollowerGrowthDTO.builder()
                                .kpiDate(fge.getKpiDate())
                                .followerCount(fge.getFollowerCount())
                                .build())
                        .toList();

        redisTemplate.opsForValue().set(cacheKey, followerGrowthDTOS, CACHE_TTL);
        return followerGrowthDTOS;
    }

    private String buildCacheKey(UUID influencerId, Platform platform, LocalDate startDate, LocalDate endDate, String analyze){
        return String.format(
                "analytics:%s:%s:%s:%s:%s",
                analyze,
                influencerId,
                platform.name(),
                startDate,
                endDate
        );
    }

}
