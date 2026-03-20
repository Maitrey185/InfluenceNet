package com.project.InfluenceNet.collaborationNeo4jService.nodes;

import lombok.*;
import org.springframework.data.neo4j.core.schema.*;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@RelationshipProperties
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PotentialCollabRelationship {

    @RelationshipId
    private Long id;

    @TargetNode
    private InfluencerNode target;

    private Integer score;
    private String reason;
    
    private ZonedDateTime updatedAt;
    private ZonedDateTime expiresAt;
}
