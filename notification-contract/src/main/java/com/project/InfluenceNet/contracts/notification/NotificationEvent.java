package com.project.InfluenceNet.contracts.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationEvent {

    private UUID userId;

    private String platform;

    private NotificationType notificationType;

    private Instant timestamp;

    private Payload payload;

}
