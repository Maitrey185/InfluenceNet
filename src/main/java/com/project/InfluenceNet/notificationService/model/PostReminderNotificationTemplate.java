package com.project.InfluenceNet.notificationService.model;

import com.project.InfluenceNet.contracts.notification.NotificationEvent;
import com.project.InfluenceNet.contracts.notification.PostReminderPayload;
import org.springframework.stereotype.Component;

@Component
public class PostReminderNotificationTemplate extends NotificationTemplate{
    @Override
    public String getType() {
        return "POST_REMINDER";
    }

    @Override
    public NotificationRequest render(NotificationEvent notificationEvent) {
        PostReminderPayload p = (PostReminderPayload) notificationEvent.getPayload();

        return NotificationRequest.builder()
                .userId(notificationEvent.getUserId())
                .title("Time to Post !")
                .body("It's time to post according to your posting schedule, lets keep growing !")
                .email(p.getEmail())
                .build();
    }
}
