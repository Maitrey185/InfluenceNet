package com.project.InfluenceNet.collaborationNeo4jService.nodes;

import lombok.*;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@RelationshipProperties
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InfluencerPostRelation {

    @RelationshipId
    private Long id;

    @TargetNode
    private PostNode postNode;
}
