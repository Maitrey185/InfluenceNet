package com.project.InfluenceNet.analyticsService.repository;

import com.project.InfluenceNet.analyticsService.entity.InfluencerKPI;
import com.project.InfluenceNet.socialConnector.documents.Platform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InfluencerKPIRepository extends JpaRepository<InfluencerKPI, UUID> {

    Optional<InfluencerKPI> findByInfluencerIdAndPlatformAndKpiDate(UUID influencerId, Platform platform, LocalDate kpiDate);
}
