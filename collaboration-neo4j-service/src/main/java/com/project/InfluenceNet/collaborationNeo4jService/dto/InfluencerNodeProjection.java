package com.project.InfluenceNet.collaborationNeo4jService.dto;

import java.util.UUID;

public interface InfluencerNodeProjection {
    UUID getId();
    String getName();
    String getEmail();
    String getPrimaryPlatform();
}
