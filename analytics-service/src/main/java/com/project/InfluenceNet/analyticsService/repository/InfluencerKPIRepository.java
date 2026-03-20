package com.project.InfluenceNet.analyticsService.repository;

import com.project.InfluenceNet.analyticsService.dto.FollowerGrowthProjection;
import com.project.InfluenceNet.analyticsService.dto.OverviewAggProjection;
import com.project.InfluenceNet.analyticsService.entity.InfluencerKPI;
import com.project.InfluenceNet.contracts.InfluencerPostContract.Platform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InfluencerKPIRepository extends JpaRepository<InfluencerKPI, UUID> {

    Optional<InfluencerKPI> findByInfluencerIdAndPlatformAndKpiDate(UUID influencerId, Platform platform, LocalDate kpiDate);

    @Query("""
       SELECT
           COALESCE(SUM(k.postCount), 0)      AS totalPosts,
           COALESCE(SUM(k.totalLikes), 0)     AS totalLikes,
           COALESCE(SUM(k.totalComments), 0)  AS totalComments,
           COALESCE(SUM(k.totalShares), 0)    AS totalShares,
           COALESCE(SUM(k.totalSaves), 0)     AS totalSaves,
           COALESCE(SUM(k.totalReach), 0)     AS totalReach,
           COALESCE(SUM(k.totalViews), 0)     AS totalViews
       FROM InfluencerKPI k
       WHERE k.influencerId = :influencerId
         AND k.platform     = :platform
         AND k.kpiDate BETWEEN :startDate AND :endDate
       """)
    OverviewAggProjection aggregateWindow(
            @Param("influencerId") UUID influencerId,
            @Param("platform") Platform platform, // enum stored as string
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );


    @Query(value = """
            SELECT 
            COALESCE(followers_count, 0) AS followerCount
            FROM influencer_kpi
            WHERE influencer_id = :influencerId
              AND platform      = :platform
              AND kpi_date         <= :date
            ORDER BY kpi_date DESC
            LIMIT 1
            """, nativeQuery = true)
    FollowerGrowthProjection findLatestFollowersBeforeOrOnDate(
            @Param("influencerId") UUID influencerId,
            @Param("platform") String platform, // enum stored as string
            @Param("date") LocalDate date
    );

    @Query(
            "SELECT" +
                    "    SUM(postCount)              AS total_posts, " +
                    "    SUM(totalLikes)              AS total_likes, " +
                    "    SUM(totalComments)           AS total_comments, " +
                    "    SUM(totalShares)             AS total_shares, " +
                    "    SUM(totalSaves)              AS total_saves, " +
                    "    SUM(totalReach)             AS total_reach, " +
                    "    SUM(totalViews)              AS total_views, " +
                    "    AVG(avgEngagementRate)      AS avg_engagement_rate " +
                    "FROM InfluencerKPI " +
                    "WHERE influencerId = :id " +
                    "  AND platform = :platform " +
                    "  AND kpiDate BETWEEN :startDate AND :endDate "
    )
    List<Object[]> calculateOverview(@Param("id") UUID id, @Param("platform") Platform platform, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    List<InfluencerKPI> findByInfluencerIdAndPlatformAndKpiDateBetween(UUID id, Platform platform, LocalDate startDate, LocalDate endDate);

    @Query(value = """
            SELECT 
            followers_count AS followerCount,
            kpi_date        AS kpiDate
            FROM influencer_kpi
            WHERE influencer_id = :influencerId
              AND platform      = :platform
              AND kpi_date BETWEEN :startDate AND :endDate
            ORDER BY kpi_date DESC
            """, nativeQuery = true)
    List<FollowerGrowthProjection> findFollowersGrowth(
            @Param("influencerId") UUID influencerId,
            @Param("platform") String platform, // enum stored as string
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
