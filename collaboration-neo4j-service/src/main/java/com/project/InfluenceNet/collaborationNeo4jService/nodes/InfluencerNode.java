package com.project.InfluenceNet.collaborationNeo4jService.nodes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.LocalDate;
import java.util.HashSet;
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
    private String email;
    private String primaryPlatform;
    private String primaryNiche;

    private Integer followerCount;
    private Integer platformAgeDays;
    private Double engagementRate;

    private Double growthRate30d;
    private String growthTrend;

    private Double postsPerWeek;
    private LocalDate joinedAt;

    private Double consistencyScore;

    @Relationship(type = "BELONGS_TO", direction = Relationship.Direction.OUTGOING)
    private Set<InfluencerNicheRelation> niches;

    @Relationship(type = "POSTED", direction = Relationship.Direction.OUTGOING)
    private Set<InfluencerPostRelation> posts;

    @Relationship(type = "MENTIONED")
    private Set<MentionedRelationship> mentioned = new HashSet<>();

    @Relationship(type = "CO_POSTED_WITH")
    private Set<CoPostedWithRelationship> coPostedWith = new HashSet<>();

    @Relationship(type = "INTERESTED_IN", direction = Relationship.Direction.OUTGOING)
    private Set<InterestedInRelationship> interestedIn = new HashSet<>();

    @Relationship(type = "SIMILAR_GROWTH", direction = Relationship.Direction.OUTGOING)
    private Set<SimilarGrowthRelationship> similarGrowth = new HashSet<>();

    @Relationship(type = "POTENTIAL_COLLAB", direction = Relationship.Direction.OUTGOING)
    private Set<PotentialCollabRelationship> potentialCollabs = new HashSet<>();

}
