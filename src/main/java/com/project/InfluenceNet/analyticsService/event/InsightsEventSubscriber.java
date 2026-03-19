package com.project.InfluenceNet.analyticsService.event;

import com.project.InfluenceNet.analyticsService.exception.PostInsightNotFoundException;
import com.project.InfluenceNet.analyticsService.service.AnalyticsService;
import com.project.InfluenceNet.analyticsService.service.EngagementHeatmapService;
import com.project.InfluenceNet.contracts.posts.RawInsights;
import com.project.InfluenceNet.socialConnector.events.InsightsfetchedEvent;
import com.project.InfluenceNet.socialConnector.repository.RawInsightsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class InsightsEventSubscriber {

    public static final String TOPIC_INSIGHT_FETCHED = "insight.fetched";
    private final RawInsightsRepository rawInsightsRepository;
    private final AnalyticsService analyticsService;
    private final EngagementHeatmapService engagementHeatmapService;

    @RetryableTopic(
            attempts = "3",
            backoff = @Backoff(delay = 2000),
            autoCreateTopics = "true"
    )
//    @KafkaListener(topics = TOPIC_INSIGHT_FETCHED)
    public void handleInsightsEvent(InsightsfetchedEvent event){
        log.info("Received insights event: {}", event);

        RawInsights rawInsights = rawInsightsRepository.findById(event.getPostId())
                .orElseThrow(() -> new PostInsightNotFoundException("Post not found with id: " + event.getPostId()));

        analyticsService.processRawInsight(rawInsights);
    }

    @DltHandler
    public void handleDlt(InsightsfetchedEvent event) {
        log.info("Final failure for InsightsfetchedEvent. Sending to DLT: {}", event);
    }
}
