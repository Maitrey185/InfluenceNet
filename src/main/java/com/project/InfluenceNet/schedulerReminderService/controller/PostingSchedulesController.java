package com.project.InfluenceNet.schedulerReminderService.controller;

import com.project.InfluenceNet.schedulerReminderService.dto.PostingScheduleDTO;
import com.project.InfluenceNet.schedulerReminderService.entity.ReminderStatus;
import com.project.InfluenceNet.schedulerReminderService.service.ReminderService;
import com.project.InfluenceNet.socialConnector.documents.Platform;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posting-schedules")
public class PostingSchedulesController {

    private final ReminderService reminderService;

    @PostMapping("/create/{influencerId}/{platform}")
    public PostingScheduleDTO createReminder(
            @PathVariable UUID influencerId,
            @PathVariable Platform platform,
            @RequestParam Set<Timestamp> schedules
    ){
        return reminderService.createReminder(influencerId, platform, schedules);
    }

    @PatchMapping
    public PostingScheduleDTO addSchedule(
            @PathVariable UUID influencerId,
            @PathVariable Platform platform,
            @RequestParam Timestamp schedule
    ){
        return reminderService.addSchedule(influencerId, platform, schedule);
    }

    @PatchMapping("/delete/{influencerId}/{platform}")
    public PostingScheduleDTO deleteSchedule(
            @PathVariable UUID influencerId,
            @PathVariable Platform platform,
            @RequestParam Timestamp schedule
    ){
        return reminderService.removeSchedule(influencerId, platform, schedule);
    }

    @PatchMapping("/update/{influencerId}/{platform}")
    public PostingScheduleDTO updateReminderStatus(
            @PathVariable UUID influencerId,
            @PathVariable Platform platform,
            @RequestParam ReminderStatus reminderStatus
    ){
        return reminderService.unpdateReminderStatus(influencerId, platform, reminderStatus);
    }

    @DeleteMapping("/delete/{influencerId}/{platform}")
    public void deletePostingSchedule(
            @PathVariable UUID influencerId,
            @PathVariable Platform platform
    ){
        reminderService.deletePostingSchedule(influencerId, platform);
    }

}
