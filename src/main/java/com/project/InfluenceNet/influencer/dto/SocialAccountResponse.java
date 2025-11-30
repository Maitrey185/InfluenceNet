package com.project.InfluenceNet.influencer.dto;

import com.project.InfluenceNet.socialConnector.documents.Platforms;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocialAccountResponse {

    private UUID id;
    @Enumerated(EnumType.STRING)
    private UUID influencerId;
    private Platforms platform;
    private String platformUserId;
    private Integer followerCount;
    private Boolean isActive;
    private Double engagementRate;
    private String accessToken;
    private String refreshToken;
    private LocalDateTime tokenExpiresAt;
}
