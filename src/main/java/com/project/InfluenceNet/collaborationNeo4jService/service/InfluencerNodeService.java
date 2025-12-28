package com.project.InfluenceNet.collaborationNeo4jService.service;

import com.project.InfluenceNet.collaborationNeo4jService.nodes.InfluencerNode;
import com.project.InfluenceNet.collaborationNeo4jService.repository.InfluencerNodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InfluencerNodeService {

    private final InfluencerNodeRepository influencerNodeRepository;

    @Transactional(transactionManager = "neo4jTransactionManager")
    public InfluencerNode createInfluencerNode(InfluencerNode node){
        return influencerNodeRepository.save(node);
    }


}
