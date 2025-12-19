package com.project.InfluenceNet.schedulerReminderService.entity;

import com.project.InfluenceNet.socialConnector.documents.Platform;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Builder
@Data
@Table(name="posting_schedules")
public class PostingSchedules {

    @Id
    private UUID id;

    @Column(name="influencer_id")
    private UUID influencerId;

    @Column(name="platform")
    private String platform;

    @Column(name="schedules")
    private Set<Timestamp> schedules;

    @Column(name="content")
    private String content;

    @Column(name="reminder_status")
    private String reminderStatus;

    @Column(name="created_at")
    private Timestamp created_at;

    @Column(name="updated_at")
    private Timestamp updated_at;
}
