package com.project.InfluenceNet.schedulerReminderService.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReminderService {

    private final PostingSchedulesService postingSchedulesService;
    private final NotificationService notificationService;


}
