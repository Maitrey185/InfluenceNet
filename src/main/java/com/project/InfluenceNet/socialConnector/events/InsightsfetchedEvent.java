package com.project.InfluenceNet.socialConnector.events;

import com.project.InfluenceNet.contracts.posts.Platform;
import lombok.*;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class InsightsfetchedEvent {
    private String postId;
    private Platform platform;
    private Instant timestamp;
}
