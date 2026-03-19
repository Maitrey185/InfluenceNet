package com.project.InfluenceNet.analyticsService.service;

import com.project.InfluenceNet.analyticsService.dto.BestPostingTimeHeatmapResponse;
import com.project.InfluenceNet.analyticsService.repository.PostAnalyticsRepository;
import com.project.InfluenceNet.contracts.posts.Platform;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EngagementHeatmapService {

    private final PostAnalyticsRepository postAnalyticsRepository;
    private final AnalyticsService analyticsService;

    private final RedisTemplate<String, Object> redisTemplate;

    private static final Duration HEATMAP_TTL = Duration.ofHours(24);

    public BestPostingTimeHeatmapResponse getBestPostingTimeHeatmap(UUID influencerId, Platform platform, LocalDate startDate, LocalDate endDate) {

        String cacheKey =buildCacheKey(influencerId, platform, startDate, endDate);

        BestPostingTimeHeatmapResponse cache = (BestPostingTimeHeatmapResponse) redisTemplate.opsForValue().get(cacheKey);

        if(cache==null){ // cache miss
            cache = analyticsService.fetchEngagementHeatmap(influencerId, platform, startDate, endDate);
            redisTemplate.opsForValue().set(cacheKey, cache, HEATMAP_TTL);
        }

        return cache;

    }

    private String buildCacheKey(UUID influencerId, Platform platform, LocalDate startDate, LocalDate endDate){
        return String.format(
                "analytics:heatmap:%s:%s:%s:%s",
                influencerId,
                platform.name(),
                startDate,
                endDate
        );
    }

    public void invalidateHeatmapCache(UUID influencerId, Platform platform) {

        String pattern = String.format(
                "analytics:heatmap:%s:%s:*",
                influencerId,
                platform.name()
        );

        Set<String> keys = redisTemplate.keys(pattern);

        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }


}
