package com.project.InfluenceNet.analyticsService.repository;

import com.project.InfluenceNet.analyticsService.entity.InfluencerKPI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InfluencerKPIRepository extends JpaRepository<InfluencerKPI, UUID> {
}
