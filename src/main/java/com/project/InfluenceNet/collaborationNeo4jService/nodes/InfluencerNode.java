package com.project.InfluenceNet.collaborationNeo4jService.nodes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Node("Influencer")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InfluencerNode {

    @Id
    private UUID id;

    private String name;
    private String primaryPlatform;
    private String primaryNiche;

    private Integer followerCount;
    private Integer platformAgeDays;
    private Double engagementRate;

    private Double growthRate30d;
    private String growthTrend;

    private Double postsPerWeek;
    private Date joinedAt;

    private Double consistencyScore;

    @Relationship(type = "BELONGS_TO", direction = Relationship.Direction.OUTGOING)
    private Set<InfluencerNicheRelation> niches;

    @Relationship(type = "POSTED", direction = Relationship.Direction.OUTGOING)
    private Set<InfluencerPostRelation> posts;

}
