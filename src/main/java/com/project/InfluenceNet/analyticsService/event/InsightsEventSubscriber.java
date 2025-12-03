package com.project.InfluenceNet.analyticsService.event;

import com.project.InfluenceNet.socialConnector.documents.RawInsights;
import com.project.InfluenceNet.socialConnector.events.InsightsfetchedEvent;
import com.project.InfluenceNet.socialConnector.repository.RawInsightsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class InsightsEventSubscriber {

    public static final String TOPIC_INSIGHT_FETCHED = "insight.fetched";
    private final RawInsightsRepository rawInsightsRepository;

    @KafkaListener(topics = TOPIC_INSIGHT_FETCHED)
    public void handleInsightsEvent(InsightsfetchedEvent event){
        log.info("Received insights event: {}", event);

        RawInsights rawInsights = rawInsightsRepository.findById(event.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + event.getPostId()));



    }

}
