package com.project.InfluenceNet.socialConnector.events;

import com.project.InfluenceNet.contracts.posts.RawInsights;
import com.project.InfluenceNet.contracts.posts.RawPosts;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class PostAndInsightFetchedEventPublisher {

    public static final String TOPIC_POST_FETCHED = "post.fetched";

    private final KafkaTemplate<String, PostFetchedEvent> kafkaTemplate;

    public static final String TOPIC_INSIGHT_FETCHED = "insight.fetched";

    private final KafkaTemplate<String, InsightsfetchedEvent> kafkaTemplate2;

    public void publishPostFetchedEvent(RawPosts rawPosts){

        PostFetchedEvent event = PostFetchedEvent.builder()
                .influencerId(rawPosts.getInfluencer_id())
                .postId(rawPosts.getId())
                .platform(rawPosts.getPlatform())
                .timestamp(Instant.now())
                .build();
        kafkaTemplate.send(TOPIC_POST_FETCHED, event);
    }

    public void publishInsightsFetchedEvent(RawInsights rawInsights){

        InsightsfetchedEvent event = InsightsfetchedEvent.builder()
                .postId(rawInsights.getId())
                .platform(rawInsights.getPlatform())
                .timestamp(Instant.now())
                .build();
        kafkaTemplate2.send(TOPIC_INSIGHT_FETCHED, event);
    }
}
