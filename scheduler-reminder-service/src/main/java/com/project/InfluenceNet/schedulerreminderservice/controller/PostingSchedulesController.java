package com.project.InfluenceNet.schedulerreminderservice.controller;


import com.project.InfluenceNet.schedulerreminderservice.dto.PostingScheduleDTO;
import com.project.InfluenceNet.schedulerreminderservice.entity.FrequencyType;
import com.project.InfluenceNet.schedulerreminderservice.entity.ReminderStatus;
import com.project.InfluenceNet.schedulerreminderservice.service.PostingSchedulesService;
import com.project.InfluenceNet.schedulerreminderservice.entity.Platform;
import com.project.InfluenceNet.schedulerreminderservice.service.ReminderScheduler;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.UUID;

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
            @RequestParam String email,
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
        return postingSchedulesService.createReminder(influencerId, email, platform, frequencyType, frequencyValue, startTime);
    }

    @PatchMapping("/update/{influencerId}/{platform}")
    public PostingScheduleDTO updateReminderStatus(
            @PathVariable UUID influencerId,
            @PathVariable Platform platform,
            @RequestParam ReminderStatus reminderStatus
    ){
        return postingSchedulesService.unpdateReminderStatus(influencerId, platform, reminderStatus);
    }

    @PostMapping("/send-reminder")
    public void sendReminder() {
        reminderScheduler.checkPostingReminders();
    }
}
