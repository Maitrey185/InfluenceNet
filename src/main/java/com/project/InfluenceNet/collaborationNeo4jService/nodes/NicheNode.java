package com.project.InfluenceNet.collaborationNeo4jService.nodes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.UUID;

@Node("Niche")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NicheNode {

    @Id
    private String id;

    private String name;

}
