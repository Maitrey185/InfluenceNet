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
public class SimilarGrowthRelationship {

    @RelationshipId
    private Long id;

    @TargetNode
    private InfluencerNode target;

    private Double diff;
    
    private ZonedDateTime updatedAt;
}
