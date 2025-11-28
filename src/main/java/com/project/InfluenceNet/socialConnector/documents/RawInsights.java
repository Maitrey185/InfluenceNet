package com.project.InfluenceNet.socialConnector.documents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Document(collection = "raw_insights")
public class RawInsights {

    @Id
    private ObjectId id;

    private Enum<Platforms> platform;

    private int likes;
    private int comments;
    private int shares;
    private int saves;
    private int reach;
    private int impressions;
    private Date fetchedAt;
    private int ig_reels_video_view_total_time;
    private double ig_reels_avg_watch_time;
    private int total_interactions;
    private int views;

}
