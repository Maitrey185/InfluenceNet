package com.project.InfluenceNet.socialConnector.documents;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "raw_insights")
public class RawInsights {

    @Id
    private String id;

    private Platform platform;

    private int likes;
    private int comments;
    private int shares;
    private int saves;
    private int reach;

    private LocalDate fetched_at;
    private int ig_reels_video_view_total_time;
    private double ig_reels_avg_watch_time;
    private int total_interactions;
    private int views;

}
