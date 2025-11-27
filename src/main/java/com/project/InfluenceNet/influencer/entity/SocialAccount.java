package com.project.InfluenceNet.influencer.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "social_accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocialAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "influencer_id", nullable = false)
    @JsonBackReference
    private InfluencerProfile influencer;

    @Column(nullable = false, length = 50)
    private String platform; // 'instagram', 'youtube', 'tiktok', 'twitter'

    @Column(name = "platform_user_id")
    private String platformUserId;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name="token_expires_at")
    private LocalDateTime tokenExpiresAt;

    @Column(name = "follower_count")
    private Integer followerCount;

    @Column(name = "engagement_rate")
    private Double engagementRate;



}
