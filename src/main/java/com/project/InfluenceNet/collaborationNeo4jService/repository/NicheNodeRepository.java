package com.project.InfluenceNet.collaborationNeo4jService.repository;

import com.project.InfluenceNet.collaborationNeo4jService.nodes.NicheNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NicheNodeRepository extends Neo4jRepository<NicheNode, String> {
}
