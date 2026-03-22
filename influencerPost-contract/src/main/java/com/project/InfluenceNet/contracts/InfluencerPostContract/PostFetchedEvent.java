package com.project.InfluenceNet.contracts.InfluencerPostContract;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PostFetchedEvent {
    private UUID influencerId;
    private String postId;
    private Platform platform;
    private Instant timestamp;
}
