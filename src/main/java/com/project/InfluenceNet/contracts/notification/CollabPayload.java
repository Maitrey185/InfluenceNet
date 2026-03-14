package com.project.InfluenceNet.contracts.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class CollabPayload implements Payload {

    private List<String> collaboratorName;

    private double score;

    private String email;


}
