package com.project.InfluenceNet.influencer.dto;

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
    @Pattern(regexp = "instagram|youtube|twitter", message = "Platform must be one of: instagram, youtube, twitter")
    private String platform;

    @NotBlank(message = "UserId is required")
    private String platformUserId;


    private String accessToken;
    private String refreshToken;
    private LocalDateTime tokenExpiresAt;
    private Integer followerCount;
    private Double engagementRate;
}
