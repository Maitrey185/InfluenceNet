package com.project.InfluenceNet.collaborationNeo4jService.service;

import com.project.InfluenceNet.collaborationNeo4jService.nodes.InfluencerNode;
import com.project.InfluenceNet.collaborationNeo4jService.nodes.PostNode;
import com.project.InfluenceNet.collaborationNeo4jService.repository.InfluencerNodeRepository;
import com.project.InfluenceNet.collaborationNeo4jService.repository.PostNodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostNodeService {

    private final PostNodeRepository postNodeRepository;

    @Transactional(transactionManager = "neo4jTransactionManager")
    public PostNode saveOrUpdate(PostNode node){
        return postNodeRepository.save(node);
    }

    public PostNode getById(String id) {
        return postNodeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found: " + id));
    }

    /* DELETE */
    @Transactional
    public void delete(String id) {
        postNodeRepository.deleteById(id);
    }
}
