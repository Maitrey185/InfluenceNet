package com.project.InfluenceNet.schedulerreminderservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalTime;
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

    @Column(name = "recipient_email")
    private String recipientEmail;

    @Enumerated(EnumType.STRING)
    @Column(name="platform")
    private Platform platform;

    @Column(name="frequency_type")
    @Enumerated(EnumType.STRING)
    private FrequencyType frequencyType;

    @Column(name="frequency_value")
    private Integer frequencyValue;

    @Column(name="start_time")
    private LocalTime startTime;

    @Column(name="last_reminder_sent_at")
    private Timestamp lastReminderSentAt;

    @Column(name="content")
    private String content;

    @Column(name="reminder_status")
    @Enumerated(EnumType.STRING)
    private ReminderStatus reminderStatus;

    @Column(name="created_at")
    private Timestamp created_at;

    @Column(name="updated_at")
    private Timestamp updated_at;
}
