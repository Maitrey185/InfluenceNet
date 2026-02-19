package com.project.InfluenceNet.schedulerReminderService.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class CollabPayload implements Payload {

    private String collaboratorName;

    private double score;

    private String email;
}
