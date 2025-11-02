package com.project.InfluenceNet.influencer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InfluencerProfileResponse {

    private UUID id;
    private String email;
    private String username;
    private Integer totalFollowerCount;
    private Double avgEngagementRate;
    private Boolean isActive;
    private List<SocialAccountResponse> socialAccounts;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
