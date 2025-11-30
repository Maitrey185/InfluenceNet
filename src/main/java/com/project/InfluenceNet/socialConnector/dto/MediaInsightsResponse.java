package com.project.InfluenceNet.socialConnector.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MediaInsightsResponse {
    List<MediaInsights> mediaInsights;
}
