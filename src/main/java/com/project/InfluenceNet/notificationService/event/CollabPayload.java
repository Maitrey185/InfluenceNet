package com.project.InfluenceNet.notificationService.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class CollabPayload implements Payload{

    private String collaboratorName;

    private double score;

    private String email;
}
