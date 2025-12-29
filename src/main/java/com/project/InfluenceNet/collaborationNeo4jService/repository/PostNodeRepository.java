package com.project.InfluenceNet.collaborationNeo4jService.repository;

import com.project.InfluenceNet.collaborationNeo4jService.nodes.PostNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostNodeRepository extends Neo4jRepository<PostNode,String> {
}
