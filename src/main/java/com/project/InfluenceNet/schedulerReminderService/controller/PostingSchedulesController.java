package com.project.InfluenceNet.schedulerReminderService.controller;

import com.project.InfluenceNet.schedulerReminderService.dto.PostingScheduleDTO;
import com.project.InfluenceNet.schedulerReminderService.entity.ReminderStatus;
import com.project.InfluenceNet.schedulerReminderService.service.PostingSchedulesService;
import com.project.InfluenceNet.socialConnector.documents.Platform;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posting-schedules")
public class PostingSchedulesController {

    private final PostingSchedulesService postingSchedulesService;

    @PostMapping("/create/{influencerId}/{platform}")
    public PostingScheduleDTO createReminder(
            @PathVariable UUID influencerId,
            @PathVariable Platform platform,
            @RequestParam Set<String> schedules
    ){
        return postingSchedulesService.createReminder(influencerId, platform, parseSchedules(schedules));
    }

    @PatchMapping("/add/{influencerId}/{platform}")
    public PostingScheduleDTO addSchedule(
            @PathVariable UUID influencerId,
            @PathVariable Platform platform,
            @RequestParam String schedule
    ){
        return postingSchedulesService.addSchedule(influencerId, platform, parseSchedule(schedule));
    }

    @PatchMapping("/delete/{influencerId}/{platform}")
    public PostingScheduleDTO deleteSchedule(
            @PathVariable UUID influencerId,
            @PathVariable Platform platform,
            @RequestParam String schedule
    ){
        return postingSchedulesService.removeSchedule(influencerId, platform, parseSchedule(schedule));
    }

    @PatchMapping("/update/{influencerId}/{platform}")
    public PostingScheduleDTO updateReminderStatus(
            @PathVariable UUID influencerId,
            @PathVariable Platform platform,
            @RequestParam ReminderStatus reminderStatus
    ){
        return postingSchedulesService.unpdateReminderStatus(influencerId, platform, reminderStatus);
    }

    @DeleteMapping("/delete/{influencerId}/{platform}")
    public void deletePostingSchedule(
            @PathVariable UUID influencerId,
            @PathVariable Platform platform
    ){
        postingSchedulesService.deletePostingSchedule(influencerId, platform);
    }

    private Set<LocalTime> parseSchedules(Set<String> schedules) {

        return schedules.stream()
                .map(LocalTime::parse)
                .collect(Collectors.toSet());
    }

    private LocalTime parseSchedule(String schedule) {
        return LocalTime.parse(schedule);
    }


}
