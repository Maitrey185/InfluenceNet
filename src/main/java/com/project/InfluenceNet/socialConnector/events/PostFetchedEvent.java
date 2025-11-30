package com.project.InfluenceNet.socialConnector.events;

import com.project.InfluenceNet.socialConnector.documents.Platforms;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class PostFetchedEvent {
    private UUID influencerId;
    private String postId;
    private Platforms platform;
    private Instant timestamp;
}
