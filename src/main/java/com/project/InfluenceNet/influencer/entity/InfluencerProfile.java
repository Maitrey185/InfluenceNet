package com.project.InfluenceNet.influencer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(name = "influencer_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InfluencerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "avg_engagement_rate")
    private Double avgEngagementRate = 0.0;

    @Column(name="total_follower_count")
    private Integer totalFollowerCount = 0;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @OneToMany(mappedBy = "influencer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<SocialAccount> socialAccounts = new ArrayList<>();

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
