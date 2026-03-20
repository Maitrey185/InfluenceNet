package com.project.InfluenceNet.collaborationNeo4jService.nodes;

import lombok.*;
import org.springframework.data.neo4j.core.schema.*;

@RelationshipProperties
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InfluencerNicheRelation {

    @RelationshipId
    private Long id;

    @TargetNode
    private NicheNode niche;

}
