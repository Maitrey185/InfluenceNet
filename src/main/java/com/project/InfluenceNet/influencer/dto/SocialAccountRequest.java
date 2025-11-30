package com.project.InfluenceNet.influencer.dto;

import com.project.InfluenceNet.socialConnector.documents.Platforms;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocialAccountRequest {

    @NotBlank(message = "Platform is required")
    @Enumerated(EnumType.STRING)
    private Platforms platform;

    @NotBlank(message = "UserId is required")
    private String platformUserId;


    private String accessToken;
    private String refreshToken;
    private LocalDateTime tokenExpiresAt;
    private Integer followerCount;
    private Double engagementRate;
}
