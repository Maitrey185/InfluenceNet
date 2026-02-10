package com.project.InfluenceNet.schedulerReminderService.service;

import com.project.InfluenceNet.schedulerReminderService.entity.PostingSchedules;
import com.project.InfluenceNet.schedulerReminderService.repository.PostingSchedulesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReminderScheduler {

    private final PostingSchedulesRepository repo;
    private final ReminderService reminderService;

    @Scheduled(fixedRate = 300000) // 5 min
    public void checkPostingReminders() {

        List<PostingSchedules> schedules = repo.findAll();

        for (PostingSchedules ps : schedules) {

            if (reminderService.shouldSendReminder(ps)) {
                reminderService.sendReminder(ps);
                ps.setLastReminderSentAt(new Timestamp(System.currentTimeMillis()));
                repo.save(ps);
            }
        }
    }





}
