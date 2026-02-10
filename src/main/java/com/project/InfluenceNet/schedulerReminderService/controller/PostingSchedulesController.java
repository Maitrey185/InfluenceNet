package com.project.InfluenceNet.schedulerReminderService.controller;

import com.project.InfluenceNet.schedulerReminderService.dto.PostingScheduleDTO;
import com.project.InfluenceNet.schedulerReminderService.entity.FrequencyType;
import com.project.InfluenceNet.schedulerReminderService.entity.ReminderStatus;
import com.project.InfluenceNet.schedulerReminderService.service.PostingSchedulesService;
import com.project.InfluenceNet.schedulerReminderService.service.ReminderScheduler;
import com.project.InfluenceNet.schedulerReminderService.service.ReminderService;
import com.project.InfluenceNet.socialConnector.documents.Platform;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
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
    private final ReminderScheduler reminderScheduler;

    @PostMapping("/create/{influencerId}/{platform}")
    public PostingScheduleDTO createReminder(
            @PathVariable UUID influencerId,
            @PathVariable Platform platform,
            @RequestParam FrequencyType frequencyType,
            @RequestParam Integer frequencyValue,
            @Parameter(
                    description = "Start time",
                    example = "09:00",
                    schema = @Schema(
                            type = "string",
                            pattern = "^([01]\\d|2[0-3]):([0-5]\\d)$"
                    )
            )
            @DateTimeFormat(pattern = "HH:mm") @RequestParam LocalTime startTime
    ) {
        return postingSchedulesService.createReminder(influencerId, platform, frequencyType, frequencyValue, startTime);
    }

    @PatchMapping("/update/{influencerId}/{platform}")
    public PostingScheduleDTO updateReminderStatus(
            @PathVariable UUID influencerId,
            @PathVariable Platform platform,
            @RequestParam ReminderStatus reminderStatus
    ){
        return postingSchedulesService.unpdateReminderStatus(influencerId, platform, reminderStatus);
    }


    private Set<LocalTime> parseSchedules(Set<String> schedules) {

        return schedules.stream()
                .map(LocalTime::parse)
                .collect(Collectors.toSet());
    }

    private LocalTime parseSchedule(String schedule) {
        return LocalTime.parse(schedule);
    }

    @PostMapping("/send-reminder")
    private void sendReminder() {
        reminderScheduler.checkPostingReminders();
    }

}
