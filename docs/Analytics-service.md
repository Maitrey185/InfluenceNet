Nice, this is a clean spec. Let’s turn it into a concrete Spring Boot design with controllers, DTOs, services, and Redis caching that fits your InfluenceNet project.

I’ll assume package root `com.project.InfluenceNet.analyticsService`.

---

## 1. Endpoint design (REST contracts)

### 1) Overview

**GET** `/analytics/influencers/{id}/overview`

Query params (optional):

* `startDate` (ISO date)
* `endDate` (ISO date)
* `platform` (optional, your `Platform` enum)
* `period` (optional, e.g. `last_30_days`, `custom`, etc.)

---

### 2) Time-series KPIs

**GET** `/analytics/influencers/{id}/kpis`

Query params:

* `startDate` (required)
* `endDate` (required)
* `platform` (required, `Platform`)
* optionally `interval=day|week|month`

---

### 3) Top posts

**GET** `/analytics/influencers/{id}/posts`

Query params:

* `platform` (required)
* `sort` (default `engagement`)
* `limit` (default 10)

---

### 4) Growth curve

**GET** `/analytics/influencers/{id}/growth`

Query params:

* `startDate`
* `endDate`
* `platform` (optional, if you store follower counts per platform)

---

### 5) Engagement heatmap

**GET** `/analytics/influencers/{id}/engagement-heatmap`

Query params:

* `platform` (required)
* `startDate` (optional; default last 30 days)
* `endDate` (optional)

Returns a **7 x 24 matrix**: `day_of_week` x `hour_of_day`.

---

## 2. DTOs

### Overview

```java
package com.project.InfluenceNet.analyticsService.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import java.util.Map;

@Data
@Builder
public class OverviewDTO {

    @JsonProperty("influencer_id")
    private String influencerId;

    @JsonProperty("total_posts")
    private long totalPosts;

    @JsonProperty("total_followers")
    private long totalFollowers;

    @JsonProperty("avg_engagement_rate")
    private double avgEngagementRate;

    private Map<String, PlatformOverviewDTO> platforms;

    private String period; // e.g. "last_30_days", "custom"
}
```

```java
@Data
@Builder
public class PlatformOverviewDTO {

    private long posts;

    private long followers;

    @JsonProperty("avg_likes")
    private double avgLikes;

    @JsonProperty("avg_comments")
    private double avgComments;
}
```

---

### Time-series KPIs

```java
package com.project.InfluenceNet.analyticsService.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class TimeSeriesKpiResponse {

    @JsonProperty("influencer_id")
    private String influencerId;

    private String platform;

    @JsonProperty("start_date")
    private LocalDate startDate;

    @JsonProperty("end_date")
    private LocalDate endDate;

    private String interval; // day/week/month

    private List<KpiPoint> points;
}
```

```java
@Data
@Builder
public class KpiPoint {
    private LocalDate date;

    @JsonProperty("impressions")
    private long impressions;

    @JsonProperty("reach")
    private long reach;

    @JsonProperty("likes")
    private long likes;

    @JsonProperty("comments")
    private long comments;

    @JsonProperty("shares")
    private long shares;

    @JsonProperty("engagement_rate")
    private double engagementRate;
}
```

---

### Top Posts

```java
@Data
@Builder
public class TopPostsResponse {

    @JsonProperty("influencer_id")
    private String influencerId;

    private String platform;

    private String sort;

    private int limit;

    private List<PostSummary> posts;
}
```

```java
@Data
@Builder
public class PostSummary {

    @JsonProperty("post_id")
    private String postId;

    private String url;

    private String caption;

    @JsonProperty("posted_at")
    private String postedAt; // ISO string or LocalDateTime

    @JsonProperty("likes")
    private long likes;

    @JsonProperty("comments")
    private long comments;

    @JsonProperty("shares")
    private long shares;

    @JsonProperty("engagement_score")
    private double engagementScore;
}
```

---

### Growth Curve

```java
@Data
@Builder
public class GrowthCurveResponse {

    @JsonProperty("influencer_id")
    private String influencerId;

    private String platform;

    @JsonProperty("start_date")
    private LocalDate startDate;

    @JsonProperty("end_date")
    private LocalDate endDate;

    private List<GrowthPoint> points;
}
```

```java
@Data
@Builder
public class GrowthPoint {

    private LocalDate date;

    @JsonProperty("followers")
    private long followers;
}
```

---

### Engagement Heatmap

```java
@Data
@Builder
public class EngagementHeatmapResponse {

    @JsonProperty("influencer_id")
    private String influencerId;

    private String platform;

    @JsonProperty("start_date")
    private LocalDate startDate;

    @JsonProperty("end_date")
    private LocalDate endDate;

    /**
     * 7 x 24 matrix.
     * heatmap[dayOfWeek][hour] -> engagement score
     * dayOfWeek: 0 = Monday, 6 = Sunday (your choice, just document it)
     */
    private double[][] heatmap;
}
```

---

## 3. Controller for these APIs

```java
package com.project.InfluenceNet.analyticsService.controller;

import com.project.InfluenceNet.analyticsService.dto.*;
import com.project.InfluenceNet.analyticsService.service.InfluencerAnalyticsService;
import com.project.InfluenceNet.schedulerreminderservice.entity.Platform;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping(value = "/analytics/influencers", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class InfluencerAnalyticsController {

    private final InfluencerAnalyticsService analyticsService;

    // 1. Overview
    @GetMapping("/{id}/overview")
    public ResponseEntity<OverviewDTO> getOverview(
            @PathVariable("id") UUID influencerId,
            @RequestParam(required = false) Platform platform,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false, defaultValue = "last_30_days") String period
    ) {
        OverviewDTO dto = analyticsService.getOverview(influencerId, platform, startDate, endDate, period);
        return ResponseEntity.ok(dto);
    }

    // 2. Time-series KPIs
    @GetMapping("/{id}/kpis")
    public ResponseEntity<TimeSeriesKpiResponse> getKpis(
            @PathVariable("id") UUID influencerId,
            @RequestParam Platform platform,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "day") String interval
    ) {
        return ResponseEntity.ok(
                analyticsService.getTimeSeriesKpis(influencerId, platform, startDate, endDate, interval)
        );
    }

    // 3. Top posts
    @GetMapping("/{id}/posts")
    public ResponseEntity<TopPostsResponse> getTopPosts(
            @PathVariable("id") UUID influencerId,
            @RequestParam Platform platform,
            @RequestParam(defaultValue = "engagement") String sort,
            @RequestParam(defaultValue = "10") int limit
    ) {
        return ResponseEntity.ok(
                analyticsService.getTopPosts(influencerId, platform, sort, limit)
        );
    }

    // 4. Growth curve
    @GetMapping("/{id}/growth")
    public ResponseEntity<GrowthCurveResponse> getGrowth(
            @PathVariable("id") UUID influencerId,
            @RequestParam(required = false) Platform platform,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return ResponseEntity.ok(
                analyticsService.getGrowthCurve(influencerId, platform, startDate, endDate)
        );
    }

    // 5. Engagement heatmap
    @GetMapping("/{id}/engagement-heatmap")
    public ResponseEntity<EngagementHeatmapResponse> getEngagementHeatmap(
            @PathVariable("id") UUID influencerId,
            @RequestParam Platform platform,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return ResponseEntity.ok(
                analyticsService.getEngagementHeatmap(influencerId, platform, startDate, endDate)
        );
    }
}
```

---

## 4. Service interface (business logic)

```java
package com.project.InfluenceNet.analyticsService.service;

import com.project.InfluenceNet.analyticsService.dto.*;
import com.project.InfluenceNet.schedulerreminderservice.entity.Platform;

import java.time.LocalDate;
import java.util.UUID;

public interface InfluencerAnalyticsService {

    OverviewDTO getOverview(UUID influencerId,
                            Platform platform,
                            LocalDate startDate,
                            LocalDate endDate,
                            String period);

    TimeSeriesKpiResponse getTimeSeriesKpis(UUID influencerId,
                                            Platform platform,
                                            LocalDate startDate,
                                            LocalDate endDate,
                                            String interval);

    TopPostsResponse getTopPosts(UUID influencerId,
                                 Platform platform,
                                 String sort,
                                 int limit);

    GrowthCurveResponse getGrowthCurve(UUID influencerId,
                                       Platform platform,
                                       LocalDate startDate,
                                       LocalDate endDate);

    EngagementHeatmapResponse getEngagementHeatmap(UUID influencerId,
                                                   Platform platform,
                                                   LocalDate startDate,
                                                   LocalDate endDate);
}
```

---

## 5. Engagement heatmap calculation + Redis caching

### Redis config (TTL 24h)

Gradle/Maven: add `spring-boot-starter-data-redis`.

`application.yml` example:

```yaml
spring:
  cache:
    type: redis
  data:
    redis:
      host: localhost
      port: 6379
```

Config:

```java
package com.project.InfluenceNet.analyticsService.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisCacheConfig {

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(24)); // 24-hour TTL

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .build();
    }
}
```

### Service implementation (heatmap core logic)

```java
package com.project.InfluenceNet.analyticsService.service.impl;

import com.project.InfluenceNet.analyticsService.dto.*;
import com.project.InfluenceNet.analyticsService.service.InfluencerAnalyticsService;
import com.project.InfluenceNet.schedulerreminderservice.entity.Platform;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InfluencerAnalyticsServiceImpl implements InfluencerAnalyticsService {

    private final EngagementEventRepository engagementEventRepository; // your repo, or KPI repo, etc.

    // other methods (overview, kpis, posts, growth) omitted for brevity...

    @Override
    @Cacheable(
        value = "engagementHeatmap",
        key = "T(String).format('%s:%s:%s:%s', #influencerId, #platform, #startDate, #endDate)"
    )
    public EngagementHeatmapResponse getEngagementHeatmap(UUID influencerId,
                                                          Platform platform,
                                                          LocalDate startDate,
                                                          LocalDate endDate) {
        LocalDate effectiveStart = startDate != null ? startDate : LocalDate.now().minusDays(30);
        LocalDate effectiveEnd = endDate != null ? endDate : LocalDate.now();

        // fetch engagement events for that influencer + platform + date range
        List<EngagementEvent> events =
                engagementEventRepository.findByInfluencerIdAndPlatformAndDateRange(
                        influencerId, platform, effectiveStart.atStartOfDay(), effectiveEnd.plusDays(1).atStartOfDay()
                );

        double[][] matrix = new double[7][24]; // dayOfWeek x hour

        for (EngagementEvent event : events) {
            ZonedDateTime timestamp = event.getOccurredAt(); // or LocalDateTime + zone
            DayOfWeek dow = timestamp.getDayOfWeek(); // MONDAY=1..SUNDAY=7
            int dayIndex = dow.getValue() - 1;        // 0..6
            int hour = timestamp.getHour();           // 0..23

            double engagementScore = event.getLikes()
                    + event.getComments() * 2.0
                    + event.getShares() * 3.0; // example weight

            matrix[dayIndex][hour] += engagementScore;
        }

        return EngagementHeatmapResponse.builder()
                .influencerId(influencerId.toString())
                .platform(platform.name().toLowerCase())
                .startDate(effectiveStart)
                .endDate(effectiveEnd)
                .heatmap(matrix)
                .build();
    }

    /**
     * Example: call this on new data ingestion to invalidate caches
     */
    @CacheEvict(value = "engagementHeatmap", allEntries = true)
    public void evictHeatmapCacheOnNewData(UUID influencerId, Platform platform) {
        // If you want more granular eviction, use a specific key expression instead of allEntries = true
    }
}
```

> **Cache behavior**
>
> * First call: compute heatmap, store in Redis for 24h.
> * Next calls with same `id/platform/start/end`: served from cache.
> * On new data ingestion: call `evictHeatmapCacheOnNewData()` from your ingestion pipeline.

---

## 6. Caching strategy for other endpoints

You can similarly cache:

* Overview:

  ```java
  @Override
  @Cacheable(value = "overview",
             key = "T(String).format('%s:%s:%s:%s:%s', #influencerId, #platform, #startDate, #endDate, #period)")
  public OverviewDTO getOverview(...) { ... }
  ```

* Time-series:

  ```java
  @Cacheable(value = "kpis",
             key = "T(String).format('%s:%s:%s:%s:%s', #influencerId, #platform, #startDate, #endDate, #interval)")
  public TimeSeriesKpiResponse getTimeSeriesKpis(...) { ... }
  ```

* Top posts, growth similarly.

For **cache invalidation**, wire a small `AnalyticsCacheService` that has methods like:

```java
@CacheEvict(value = {"overview", "kpis", "growth", "topPosts", "engagementHeatmap"}, allEntries = true)
public void evictAllForInfluencer(UUID influencerId) { ... }
```

and call that from your ingestion service when new KPI/post data arrives.

---

If you want, next we can:

* Integrate this with your existing `InfluencerKPIService.calculateOverview` (reuse instead of new service), and
* Design the `EngagementEvent` (or KPI) entity & repository methods to support these queries efficiently (indexes, partitions, etc.).
