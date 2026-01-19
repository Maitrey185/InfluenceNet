package com.project.InfluenceNet.notificationService.event;

import lombok.Data;

import java.util.UUID;

@Data
public class CollabPayload implements Payload{

    private String collaboratorName;

    private double score;
}
