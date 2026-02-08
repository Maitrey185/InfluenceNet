package com.project.InfluenceNet.notificationService.event;

import com.project.InfluenceNet.notificationService.model.NotificationType;
import com.project.InfluenceNet.socialConnector.documents.Platform;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class NotificationEvent {

    private UUID userId;
    private Platform platform;
    private NotificationType notificationType;
    private Instant timestamp;
    private Payload payload;
}
