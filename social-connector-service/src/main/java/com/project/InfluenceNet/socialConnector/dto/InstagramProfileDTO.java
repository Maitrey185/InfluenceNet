package com.project.InfluenceNet.socialConnector.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InstagramProfileDTO {
    private String id;
    private String name;
    private String username;
    private String profile_picture_url;
    private String biography;
    private int followersCount;
    private int followsCount;
    private int mediaCount;
}
