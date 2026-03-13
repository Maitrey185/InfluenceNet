package com.project.InfluenceNet.schedulerReminderService.repository;

import com.project.InfluenceNet.schedulerReminderService.entity.PostingSchedules;
import com.project.InfluenceNet.socialConnector.documents.Platform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PostingSchedulesRepository extends JpaRepository<PostingSchedules, UUID> {

    PostingSchedules findByInfluencerIdAndPlatform(UUID influencerId, Platform platform);

    Boolean existsByInfluencerIdAndPlatform(UUID influencerId, Platform platform);
}
