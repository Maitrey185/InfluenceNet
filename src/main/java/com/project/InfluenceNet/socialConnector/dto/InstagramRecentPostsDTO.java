package com.project.InfluenceNet.socialConnector.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InstagramRecentPostsDTO {
    private String id;
    private String media_type;
    private String media_url;
    private String thumbnail_url;
    private String caption;
    private String permalink;
    private String username;
    private String timestamp;
    private int like_count;
    private int comments_count;
}
