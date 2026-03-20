package com.project.InfluenceNet.collaborationNeo4jService.nodes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Node("Post")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostNode {

    @Id
    private String id;

    private String platform;

    private String post_type;

    private Date posted_at;

    private int reach;

    private Double engagementRate;

    @Relationship(type = "ENGAGED_WITH", direction = Relationship.Direction.INCOMING)
    private Set<EngagedWithRelationship> engagements = new HashSet<>();

}
