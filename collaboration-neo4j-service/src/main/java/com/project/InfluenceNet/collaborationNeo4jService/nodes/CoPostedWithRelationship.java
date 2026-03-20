package com.project.InfluenceNet.collaborationNeo4jService.nodes;

import lombok.*;
import org.springframework.data.neo4j.core.schema.*;


@RelationshipProperties
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoPostedWithRelationship {

    @RelationshipId
    private Long id;

    @TargetNode
    private InfluencerNode collaborator;

    private Integer times; // number of co-posts
}

