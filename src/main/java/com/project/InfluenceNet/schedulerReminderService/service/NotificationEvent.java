package com.project.InfluenceNet.schedulerReminderService.service;

import com.project.InfluenceNet.notificationService.model.NotificationType;
import com.project.InfluenceNet.schedulerReminderService.entity.Payload;
import com.project.InfluenceNet.socialConnector.documents.Platform;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class NotificationEvent {

    private UUID userId;
    private Platform platform;
    private NotificationType notificationType;
    private Instant timestamp;
    private Payload payload;
}
