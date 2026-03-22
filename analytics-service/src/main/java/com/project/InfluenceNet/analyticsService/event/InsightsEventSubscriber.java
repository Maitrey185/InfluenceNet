package com.project.InfluenceNet.analyticsService.event;

import com.project.InfluenceNet.analyticsService.service.AnalyticsService;
import com.project.InfluenceNet.analyticsService.service.EngagementHeatmapService;
import com.project.InfluenceNet.contracts.InfluencerPostContract.RawInsights;
import com.project.InfluenceNet.contracts.InfluencerPostContract.InsightsfetchedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class InsightsEventSubscriber {

    public static final String TOPIC_INSIGHT_FETCHED = "insight.fetched";
    private final AnalyticsService analyticsService;
    private final EngagementHeatmapService engagementHeatmapService;
    private final RestTemplate restTemplate;

    @RetryableTopic(
            attempts = "3",
            backoff = @Backoff(delay = 2000),
            autoCreateTopics = "true"
    )
    @KafkaListener(topics = TOPIC_INSIGHT_FETCHED)
    public void handleInsightsEvent(InsightsfetchedEvent event){
        log.info("Received insights event: {}", event);

        String socialConnectorUrl = "http://localhost:8094/instagram/rawInsights/" + event.getPostId();
        RawInsights rawInsights = restTemplate.getForObject(socialConnectorUrl, RawInsights.class);

        analyticsService.processRawInsight(rawInsights);
    }

    @DltHandler
    public void handleDlt(InsightsfetchedEvent event) {
        log.info("Final failure for InsightsfetchedEvent. Sending to DLT: {}", event);
    }
}
