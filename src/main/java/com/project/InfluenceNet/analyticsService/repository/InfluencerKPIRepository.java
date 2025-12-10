package com.project.InfluenceNet.analyticsService.repository;

import com.project.InfluenceNet.analyticsService.dto.OverviewDTO;
import com.project.InfluenceNet.analyticsService.entity.InfluencerKPI;
import com.project.InfluenceNet.socialConnector.documents.Platform;
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
}
