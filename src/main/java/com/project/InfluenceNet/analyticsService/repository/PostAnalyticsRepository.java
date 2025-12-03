package com.project.InfluenceNet.analyticsService.repository;

import com.project.InfluenceNet.analyticsService.entity.PostAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostAnalyticsRepository extends JpaRepository<PostAnalytics, String> {
}
