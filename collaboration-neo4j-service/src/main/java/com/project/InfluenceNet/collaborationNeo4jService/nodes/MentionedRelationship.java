package com.project.InfluenceNet.collaborationNeo4jService.nodes;

import lombok.*;
import org.springframework.data.neo4j.core.schema.*;


@RelationshipProperties
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MentionedRelationship {

    @RelationshipId
    private Long id;

    @TargetNode
    private InfluencerNode mentionedInfluencer;

    private Integer count; // how many times mentioned
}

