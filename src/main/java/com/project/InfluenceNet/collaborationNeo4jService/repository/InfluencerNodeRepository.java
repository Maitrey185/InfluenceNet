package com.project.InfluenceNet.collaborationNeo4jService.repository;

import com.project.InfluenceNet.collaborationNeo4jService.nodes.InfluencerNode;
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

}
