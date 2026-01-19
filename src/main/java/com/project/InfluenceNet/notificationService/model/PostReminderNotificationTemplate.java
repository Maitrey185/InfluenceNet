package com.project.InfluenceNet.notificationService.model;

import com.project.InfluenceNet.notificationService.event.CollabPayload;
import com.project.InfluenceNet.notificationService.event.NotificationEvent;

public class PostReminderNotificationTemplate extends NotificationTemplate{
    @Override
    public NotificationRequest render(NotificationEvent notificationEvent) {
        CollabPayload p = (CollabPayload) notificationEvent.getPayload();

        return NotificationRequest.builder()
                .userId(notificationEvent.getUserId())
                .title("Time to Post !")
                .body("It's time to post according to your posting schedule, lets keep growing !")
                .build();
    }
}
