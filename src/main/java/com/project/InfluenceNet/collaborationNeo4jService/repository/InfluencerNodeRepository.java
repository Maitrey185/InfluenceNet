package com.project.InfluenceNet.collaborationNeo4jService.repository;

import com.project.InfluenceNet.collaborationNeo4jService.dto.CoPostProjection;
import com.project.InfluenceNet.collaborationNeo4jService.dto.MentionIntentProjection;
import com.project.InfluenceNet.collaborationNeo4jService.dto.MutualEngagementProjection;
import com.project.InfluenceNet.collaborationNeo4jService.nodes.InfluencerNode;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InfluencerNodeRepository extends Neo4jRepository<InfluencerNode, UUID> {

    Optional<InfluencerNode> findByName(String name);

    List<InfluencerNode> findByPrimaryNiche(String primaryNiche);

    List<InfluencerNode> findByPrimaryPlatform(String primaryPlatform);

    @Query("""
    MATCH (a:Influencer {id: $fromId})
    MATCH (b:Influencer {id: $toId})
    MERGE (a)-[r:MENTIONED]->(b)
    ON CREATE SET r.count = 1
    ON MATCH SET r.count = r.count + 1
    """)
    void addMention(UUID fromId, UUID toId);

    @Query("""
    MATCH (a:Influencer {id: $id1})
    MATCH (b:Influencer {id: $id2})
    MERGE (a)-[r:CO_POSTED_WITH]->(b)
    ON CREATE SET r.times = 1
    ON MATCH SET r.times = r.times + 1
    """)
    void addCoPost(UUID id1, UUID id2);

    @Query("""
    MATCH (i:Influencer {id: $influencerId})
    MATCH (p:Post {id: $postId})
    MERGE (i)-[r:ENGAGED_WITH]->(p)
    ON CREATE SET r.likes = $likes, r.comments = $comments
    ON MATCH SET
      r.likes = r.likes + $likes,
      r.comments = r.comments + $comments
    """)
    void addEngagement(
                UUID influencerId,
                String postId,
                Integer likes,
                Integer comments
    );

    @Query("""
            MATCH (a:Influencer)-[:ENGAGED_WITH]->(:Post)<-[:ENGAGED_WITH]-(b:Influencer)
            WHERE a.id <> b.id
            RETURN {
              source: a,
              target: b,
              sharedPosts: count(*)
            } AS engagement
            ORDER BY sharedPosts DESC
    """)
    List<MutualEngagementProjection> findMutualEngagements();

    @Query("""
    MATCH (a:Influencer)-[m:MENTIONED]->(b:Influencer)
    WHERE m.count >= $minMentions
    RETURN a AS from, b AS to, m.count AS mentionCount
    ORDER BY mentionCount DESC
    """)
    List<MentionIntentProjection> findMentionIntent(int minMentions);

    @Query("""
    MATCH (a:Influencer)-[c:CO_POSTED_WITH]->(b:Influencer)
    WHERE c.times >= $minTimes
    RETURN a AS a, b AS b, c.times AS times
    ORDER BY times DESC
    """)
    List<CoPostProjection> findStrongCollaborations(int minTimes);



    @Query("""
    MATCH (a:Influencer)-[e:ENGAGED_WITH]->(p:Post)<-[:POSTED]-(b:Influencer)
    WHERE a <> b
    WITH a, b, sum(e.likes + e.comments) AS engagementScore
    WHERE engagementScore >= $minScore
    MERGE (a)-[r:INTERESTED_IN]->(b)
    SET r.score = engagementScore,
        r.updatedAt = datetime()
    """)
    void deriveInterestEdges(int minScore);

    @Query("""
        MATCH (a:Influencer), (b:Influencer) 
        WHERE a <> b 
        AND a.growthTrend = b.growthTrend 
        AND abs(a.growthRate30d - b.growthRate30d) <= 3
        MERGE (a)-[r:SIMILAR_GROWTH]->(b)
        SET r.diff = abs(a.growthRate30d - b.growthRate30d),
        r.updatedAt = datetime()
        """)
    void deriveSimilarGrowthEdges();

    @Query("""
            MATCH (a:Influencer)-[:ENGAGED_WITH]->(p:Post)<-[:ENGAGED_WITH]-(b:Influencer)
            WHERE a <> b
            WITH a, b, count(DISTINCT p) AS sharedPosts
            WHERE sharedPosts >= 3
            MERGE (a)-[r:AUDIENCE_OVERLAP]->(b)
            SET r.sharedPosts = sharedPosts,
            r.updatedAt = datetime()        
            """)
    void deriveAudienceOverlap();

    @Query("""
    MATCH (a:Influencer)-[i:INTERESTED_IN]->(b)
    MATCH (a)-[:SIMILAR_GROWTH]->(b)
    MERGE (a)-[r:POTENTIAL_COLLAB]->(b)
    SET r.score = i.score,
        r.reason = 'similar growth + mutual engagement',
        r.updatedAt = datetime(),
        r.expiresAt = datetime() + duration('P7D')
    """)
    void derivePotentialCollaborations();

    @Query("""
        MATCH (a:Influencer {id: $id})
        MATCH (b:Influencer)
        WHERE a <> b
        MATCH (a:Influencer)-[:BELONGS_TO]->(n:Niche)<-[:BELONGS_TO]-(b:Influencer)
        OPTIONAL MATCH (a)-[c:CO_POSTED_WITH]->(b)
        OPTIONAL MATCH (a)-[m:MENTIONED]->(b)
        OPTIONAL MATCH (a)-[t:TRUSTS]->(b)
        OPTIONAL MATCH (a)-[i:INTERESTED_IN]->(b)
        WITH a, b,
          coalesce(c.times, 0) * 2 +
          coalesce(m.count, 0) +
          coalesce(t.sharedCollaborators, 0) * 3 +
          coalesce(i.score, 0) AS relationshipStrength,
          (1 - abs(a.engagementRate - b.engagementRate) / 10)        AS engagementSim,
          (1 - abs(a.growthRate30d - b.growthRate30d) / 20)          AS growthSim,
          (1 - abs(a.platformAgeDays - b.platformAgeDays) / 1000)   AS ageSim,
          (1 - abs(a.postsPerWeek - b.postsPerWeek) / 10)            AS frequencySim
        RETURN b {
                     .*,
                     joinedAt:
                       CASE
                         WHEN b.joinedAt IS NULL THEN NULL
                         WHEN b.joinedAt CONTAINS "T" THEN date(datetime(b.joinedAt))
                         ELSE b.joinedAt
                       END
                   } AS b,
          relationshipStrength * 0.40 +
          ((engagementSim + growthSim + ageSim + frequencySim)/4 * 0.30) AS score
        ORDER BY score DESC
        LIMIT 10
        """)
    List<InfluencerNode> recommendCollaborators(UUID id);



}


