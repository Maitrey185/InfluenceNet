package com.project.InfluenceNet.socialConnector.events;

import com.project.InfluenceNet.socialConnector.documents.Platforms;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class InsightsfetchedEvent {
    private String postId;
    private Platforms platform;
    private Instant timestamp;
}
