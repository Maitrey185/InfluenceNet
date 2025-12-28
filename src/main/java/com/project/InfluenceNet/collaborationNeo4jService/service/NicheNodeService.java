package com.project.InfluenceNet.collaborationNeo4jService.service;

import com.project.InfluenceNet.collaborationNeo4jService.nodes.InfluencerNode;
import com.project.InfluenceNet.collaborationNeo4jService.nodes.NicheNode;
import com.project.InfluenceNet.collaborationNeo4jService.repository.InfluencerNodeRepository;
import com.project.InfluenceNet.collaborationNeo4jService.repository.NicheNodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NicheNodeService {

    private final NicheNodeRepository nicheNodeRepository;

    @Transactional(transactionManager = "neo4jTransactionManager")
    public NicheNode saveOrUpdate(NicheNode node){
        return nicheNodeRepository.save(node);
    }

    public NicheNode getById(String id) {
        return nicheNodeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Niche not found: " + id));
    }

    /* DELETE */
    @Transactional
    public void delete(String id) {
        nicheNodeRepository.deleteById(id);
    }
}
