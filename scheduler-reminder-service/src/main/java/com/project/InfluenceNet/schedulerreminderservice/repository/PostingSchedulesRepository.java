package com.project.InfluenceNet.schedulerreminderservice.repository;

import com.project.InfluenceNet.schedulerreminderservice.entity.PostingSchedules;
import com.project.InfluenceNet.schedulerreminderservice.entity.Platform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PostingSchedulesRepository extends JpaRepository<PostingSchedules, UUID> {

    PostingSchedules findByInfluencerIdAndPlatform(UUID influencerId, Platform platform);

    Boolean existsByInfluencerIdAndPlatform(UUID influencerId, Platform platform);
}
