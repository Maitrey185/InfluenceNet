package com.project.InfluenceNet.notificationService.model;

import com.project.InfluenceNet.notificationService.event.CollabPayload;
import com.project.InfluenceNet.notificationService.event.NotificationEvent;
import com.project.InfluenceNet.notificationService.event.PostRemiderPayload;
import org.springframework.stereotype.Component;

@Component
public class PostReminderNotificationTemplate extends NotificationTemplate{
    @Override
    public String getType() {
        return "POST_REMINDER";
    }

    @Override
    public NotificationRequest render(NotificationEvent notificationEvent) {
        PostRemiderPayload p = (PostRemiderPayload) notificationEvent.getPayload();

        return NotificationRequest.builder()
                .userId(notificationEvent.getUserId())
                .title("Time to Post !")
                .body("It's time to post according to your posting schedule, lets keep growing !")
                .build();
    }
}
