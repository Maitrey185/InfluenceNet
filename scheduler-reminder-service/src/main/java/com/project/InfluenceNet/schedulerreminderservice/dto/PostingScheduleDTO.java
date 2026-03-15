package com.project.InfluenceNet.schedulerreminderservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.InfluenceNet.schedulerreminderservice.entity.PostingSchedules;
import com.project.InfluenceNet.schedulerreminderservice.entity.Platform;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Builder
public class PostingScheduleDTO {

    private UUID id;
    private UUID influencer_id;
    private String recipientEmail;

    @Enumerated(EnumType.STRING)
    private Platform platform;
    private String content;
    private String reminderStatus;
    private Timestamp created_at;
    private Timestamp updated_at;
    private String frequencyType;
    private Integer frequencyValue;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    public static PostingScheduleDTO mapToPostingScheduleDTO(PostingSchedules postingSchedules){
        return PostingScheduleDTO.builder()
                .id(postingSchedules.getId())
                .influencer_id(postingSchedules.getInfluencerId())
                .recipientEmail(postingSchedules.getRecipientEmail())
                .content(postingSchedules.getContent())
                .platform(postingSchedules.getPlatform())
                .reminderStatus(postingSchedules.getReminderStatus().name())
                .created_at(postingSchedules.getCreated_at())
                .updated_at(postingSchedules.getUpdated_at())
                .frequencyType(postingSchedules.getFrequencyType().name())
                .frequencyValue(postingSchedules.getFrequencyValue())
                .startTime(postingSchedules.getStartTime())
                .build();
    }
}
