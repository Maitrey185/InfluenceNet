package com.project.InfluenceNet.notificationService.model;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class NotificationRequest {

    private UUID userId;
    private String title;
    private String body;
    private String email;
    private ChannelType channelType;

}
