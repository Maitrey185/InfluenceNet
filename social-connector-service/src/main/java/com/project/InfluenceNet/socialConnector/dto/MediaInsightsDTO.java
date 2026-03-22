package com.project.InfluenceNet.socialConnector.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MediaInsightsDTO {

    private String id;
    private int shares;
    private int comments;
    private int likes;
    private int saved;
    private int ig_reels_video_view_total_time;
    private double ig_reels_avg_watch_time;
    private int engagement;
    private int reach;
    private int impressions;
    private int views;

}
