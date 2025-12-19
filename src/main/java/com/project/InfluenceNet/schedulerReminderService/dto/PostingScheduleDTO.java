package com.project.InfluenceNet.schedulerReminderService.dto;

import com.project.InfluenceNet.schedulerReminderService.entity.PostingSchedules;
import com.project.InfluenceNet.schedulerReminderService.entity.ReminderStatus;
import com.project.InfluenceNet.socialConnector.documents.Platform;
import lombok.Builder;
import lombok.Data;


import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class PostingScheduleDTO {

    private UUID id;
    private UUID influencer_id;
    private String platform;
    private Set<Timestamp> schedules;
    private String content;
    private String reminderStatus;
    private Timestamp created_at;
    private Timestamp updated_at;

    public static PostingScheduleDTO mapToPostingScheduleDTO(PostingSchedules postingSchedules){
        return PostingScheduleDTO.builder()
                .id(postingSchedules.getId())
                .influencer_id(postingSchedules.getInfluencerId())
                .schedules(postingSchedules.getSchedules())
                .content(postingSchedules.getContent())
                .platform(postingSchedules.getPlatform())
                .reminderStatus(postingSchedules.getReminderStatus())
                .created_at(postingSchedules.getCreated_at())
                .updated_at(postingSchedules.getUpdated_at())
                .build();
    }
}
