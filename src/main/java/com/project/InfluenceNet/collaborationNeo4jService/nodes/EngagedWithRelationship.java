package com.project.InfluenceNet.collaborationNeo4jService.nodes;

import lombok.*;
import org.springframework.data.neo4j.core.schema.*;


@RelationshipProperties
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EngagedWithRelationship {

    @RelationshipId
    private Long id;

    @TargetNode
    private PostNode post;

    private Integer likes;
    private Integer comments;
}

