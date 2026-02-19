package com.project.InfluenceNet.influencer.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InfluencerNeo4jEvent {

    private UUID id;

    private String name;
    private String email;

    private Integer followerCount;
    private Double engagementRate;

}
