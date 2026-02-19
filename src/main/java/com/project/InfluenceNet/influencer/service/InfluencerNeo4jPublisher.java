package com.project.InfluenceNet.influencer.service;

import com.project.InfluenceNet.influencer.entity.InfluencerProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class InfluencerNeo4jPublisher {

    private static final String INFLUENCER_NODE_CREATE = "influencerNode.create";
    private KafkaTemplate<String, InfluencerNeo4jEvent> kafkaTemplate;

    public void createInfluencerNeo4jPublish(InfluencerNeo4jEvent influencerNeo4jEvent){
        kafkaTemplate.send(INFLUENCER_NODE_CREATE, influencerNeo4jEvent);
        log.info("Create InfluencerNeo4j event sent");
    }

    public InfluencerNeo4jEvent createInfluencerNeo4jEvent(InfluencerProfile influencerProfile){
        return InfluencerNeo4jEvent.builder()
                .id(influencerProfile.getId())
                .name(influencerProfile.getUsername())
                .email(influencerProfile.getEmail())
                .engagementRate(influencerProfile.getAvgEngagementRate())
                .build();


    }

}
