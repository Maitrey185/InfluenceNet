package com.project.InfluenceNet.notificationservice.model;

import com.project.InfluenceNet.contracts.notification.CollabPayload;
import com.project.InfluenceNet.contracts.notification.NotificationEvent;
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
                .email(p.getEmail())
                .build();
    }
}
