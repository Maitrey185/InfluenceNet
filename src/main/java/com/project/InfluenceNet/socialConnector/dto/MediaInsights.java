package com.project.InfluenceNet.socialConnector.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MediaInsights {

    private String id;
    private String name;
    private String title;
    private String description;
    private String period;
    private List<InsightValue> values;
}
