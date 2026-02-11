package com.project.InfluenceNet.notificationService.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class CollabPayload implements Payload{

    private List<String> collaboratorName;

    private double score;

    private String email;
}
