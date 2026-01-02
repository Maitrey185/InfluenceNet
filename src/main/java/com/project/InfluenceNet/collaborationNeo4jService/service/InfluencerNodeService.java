package com.project.InfluenceNet.collaborationNeo4jService.service;

import com.project.InfluenceNet.collaborationNeo4jService.nodes.InfluencerNode;
import com.project.InfluenceNet.collaborationNeo4jService.repository.InfluencerNodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InfluencerNodeService {

    private final InfluencerNodeRepository influencerNodeRepository;

    @Transactional(transactionManager = "neo4jTransactionManager")
    public InfluencerNode saveOrUpdate(InfluencerNode node){
        return influencerNodeRepository.save(node);
    }

    public InfluencerNode getById(UUID id) {
        return influencerNodeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Influencer not found: " + id));
    }

    public List<InfluencerNode> getByNiche(String niche) {
        return influencerNodeRepository.findByPrimaryNiche(niche);
    }

    /* DELETE */
    @Transactional
    public void delete(UUID id) {
        influencerNodeRepository.deleteById(id);
    }

    public void recordMention(UUID from, UUID to) {
        influencerNodeRepository.addMention(from, to);
    }

    public void recordCoPost(UUID a, UUID b) {
        influencerNodeRepository.addCoPost(a, b);
        influencerNodeRepository.addCoPost(b, a); // bidirectional
    }

    public void recordEngagement(UUID influencerId, String postId, int likes, int comments) {
        influencerNodeRepository.addEngagement(influencerId, postId, likes, comments);
    }


}
