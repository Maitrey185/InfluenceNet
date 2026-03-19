package com.project.InfluenceNet.analyticsService.repository;

import com.project.InfluenceNet.analyticsService.dto.EngagementHeatmapCellProjection;
import com.project.InfluenceNet.analyticsService.dto.TopPostProjection;
import com.project.InfluenceNet.analyticsService.entity.PostAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface PostAnalyticsRepository extends JpaRepository<PostAnalytics, String> {


    @Query(value="""
            SELECT
            post_id as postId,
            platform as platform,
            post_type as postType,
            posted_at as postedAt,
            likes as likes,
            comments as comments,
            shares as shares,
            saves as saves,
            reach as reach,
            views as views,
            engagement_rate as engagementRate
            FROM post_analytics
            WHERE influencer_id = :influencerId
              AND platform = :platform
              AND posted_at BETWEEN :startDate AND :endDate
            ORDER BY engagement_rate DESC
            LIMIT :limit;
            """, nativeQuery = true)
    public List<TopPostProjection> fetchTopPostsInAPeriod(
            @Param("influencerId") UUID influencerId,
            @Param("platform") String platform, // enum stored as string
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("limit") int limit
    );

    @Query(value = """
        SELECT
          EXTRACT(DOW FROM posted_at)::int + 1 AS dayOfWeek,
          EXTRACT(HOUR FROM posted_at)::int    AS hourOfDay,
          SUM(likes + comments + shares + saves) AS engagement
        FROM post_analytics
        WHERE influencer_id = :influencerId
          AND platform      = :platform
          AND posted_at BETWEEN :startDate AND :endDate
        GROUP BY dayOfWeek, hourOfDay
        ORDER BY dayOfWeek, hourOfDay
    """, nativeQuery = true)
    List<EngagementHeatmapCellProjection> fetchEngagementHeatmap(
            @Param("influencerId") UUID influencerId,
            @Param("platform") String platform,   // enum → string
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
