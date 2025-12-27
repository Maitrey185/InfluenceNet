package com.project.InfluenceNet.schedulerReminderService.entity;

import com.project.InfluenceNet.socialConnector.documents.Platform;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="posting_schedules")
public class PostingSchedules {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name="influencer_id")

    private UUID influencerId;

    @Enumerated(EnumType.STRING)
    @Column(name="platform")
    private Platform platform;

    @Column(name="schedules")
    private Set<LocalTime> schedules;

    @Column(name="content")
    private String content;

    @Column(name="reminder_status")
    private String reminderStatus;

    @Column(name="created_at")
    private Timestamp created_at;

    @Column(name="updated_at")
    private Timestamp updated_at;
}
