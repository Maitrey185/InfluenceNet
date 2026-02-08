package com.project.InfluenceNet.notificationService.model;

import com.project.InfluenceNet.notificationService.event.CollabPayload;
import com.project.InfluenceNet.notificationService.event.NotificationEvent;
import com.project.InfluenceNet.notificationService.event.Payload;
import org.springframework.stereotype.Component;

@Component
public class CollabNotificationTemplate extends NotificationTemplate{
    @Override
    public String getType() {
        return "COLLAB";
    }

    @Override
    public NotificationRequest render(NotificationEvent notificationEvent) {
        CollabPayload p = (CollabPayload) notificationEvent.getPayload();

        return NotificationRequest.builder()
                .userId(notificationEvent.getUserId())
                .title("New Collaboration Opportunity")
                .body("Your recommended collaborator : "+ p.getCollaboratorName())
                .build();
    }
}
