package com.project.InfluenceNet.schedulerReminderService.service;

import com.project.InfluenceNet.influencer.controller.InfluencerController;
import com.project.InfluenceNet.influencer.service.InfluencerProfileService;
import com.project.InfluenceNet.contracts.notification.NotificationEvent;
import com.project.InfluenceNet.contracts.notification.Payload;
import com.project.InfluenceNet.contracts.notification.PostReminderPayload;
import com.project.InfluenceNet.contracts.notification.NotificationType;
import com.project.InfluenceNet.schedulerReminderService.entity.PostingSchedules;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class ReminderService {

    private final PostingSchedulesService postingSchedulesService;
    private final NotificationService notificationService;
    private final ReminderNotificationPublisher reminderNotificationPublisher;
    private final InfluencerProfileService influencerProfileService;

    public boolean shouldSendReminder(PostingSchedules ps) {

        LocalTime now = LocalTime.now();

        switch (ps.getFrequencyType()) {

            case DAILY:
                return checkDaily(ps, now);

            case WEEKLY:
                return checkWeekly(ps, now);

            case INTERVAL:
                return checkInterval(ps);

            default:
                return false;
        }
    }

    private boolean checkDaily(PostingSchedules ps, LocalTime now) {

        int timesPerDay = ps.getFrequencyValue();
        LocalTime start = ps.getStartTime();

        int gapHours = 24 / timesPerDay;

        for (int i = 0; i < timesPerDay; i++) {

            LocalTime reminderTime = start.plusHours(i * gapHours);

            if (isWithin5Min(now, reminderTime)
                    && notAlreadySent(ps, reminderTime)) {
                return true;
            }
        }

        return false;
    }

    private boolean checkWeekly(PostingSchedules ps, LocalTime now) {

        LocalTime reminderTime = ps.getStartTime();

        if (!isWithin5Min(now, reminderTime))
            return false;

        Timestamp last = ps.getLastReminderSentAt();

        if (last == null)
            return true;

        long daysSinceLast =
                Duration.between(
                        last.toLocalDateTime(),
                        LocalDateTime.now()
                ).toDays();

        int gapDays = 7 / ps.getFrequencyValue();

        return daysSinceLast >= gapDays;
    }

    private boolean checkInterval(PostingSchedules ps) {

        Timestamp last = ps.getLastReminderSentAt();

        if (last == null)
            return true;

        long hoursPassed =
                Duration.between(
                        last.toLocalDateTime(),
                        LocalDateTime.now()
                ).toHours();

        return hoursPassed >= ps.getFrequencyValue();
    }

    private boolean isWithin5Min(LocalTime now, LocalTime reminderTime) {

        LocalTime start = reminderTime.minusMinutes(5);
        LocalTime end   = reminderTime.plusMinutes(5);

        return !now.isBefore(start) && !now.isAfter(end);
    }

    private boolean notAlreadySent(PostingSchedules ps, LocalTime reminderTime) {

        if (ps.getLastReminderSentAt() == null)
            return true;

        LocalTime lastSentTime =
                ps.getLastReminderSentAt()
                        .toLocalDateTime()
                        .toLocalTime();

        long diff = Math.abs(
                Duration.between(lastSentTime, reminderTime).toMinutes()
        );

        return diff > 5;
    }


    public void sendReminder(PostingSchedules ps) {

        String email = influencerProfileService.getEmailById(ps.getInfluencerId());

         Payload payload = PostReminderPayload.builder()
                .email(email)
                .build();


        NotificationEvent notificationEvent = NotificationEvent.builder()
                .userId(ps.getInfluencerId())
                .platform(ps.getPlatform().name())
                .notificationType(NotificationType.POST_REMINDER)
                .timestamp(Instant.now())
                .payload(payload)
                .build();

        reminderNotificationPublisher.publishNotificationEvent(notificationEvent);

    }




}
