package com.project.InfluenceNet.collaborationNeo4jService.service;


import com.project.InfluenceNet.collaborationNeo4jService.nodes.InfluencerNode;
import com.project.InfluenceNet.contracts.InfluencerPostContract.InfluencerNeo4jEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InfluencerNeo4jListener {

    private final InfluencerNodeService influencerNodeService;
    private static final String INFLUENCER_NODE_CREATE = "influencerNode.create";

    @KafkaListener(topics = INFLUENCER_NODE_CREATE)
    public void createInfluencerNode(InfluencerNeo4jEvent influencerNeo4jEvent){
        InfluencerNode influencerNode = InfluencerNode.builder()
                .id(influencerNeo4jEvent.getId())
                .name(influencerNeo4jEvent.getName())
                .followerCount(influencerNeo4jEvent.getFollowerCount())
                .engagementRate(influencerNeo4jEvent.getEngagementRate())
                .email(influencerNeo4jEvent.getEmail())
                .build();
        influencerNodeService.saveOrUpdate(influencerNode);
    }
}
