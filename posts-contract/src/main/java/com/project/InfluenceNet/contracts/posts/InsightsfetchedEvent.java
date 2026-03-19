package com.project.InfluenceNet.contracts.posts;

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
