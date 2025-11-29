package com.project.InfluenceNet.socialConnector.events;

import com.project.InfluenceNet.socialConnector.documents.RawInsights;
import com.project.InfluenceNet.socialConnector.documents.RawPosts;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class PostFetchedEventPublisher {

    public static final String TOPIC_POST_FETCHED = "post.fetched";

    private final KafkaTemplate<String, PostFetchedEvent> kafkaTemplate;

    public void publishPostFetchedEvent(RawPosts rawPosts){

        PostFetchedEvent event = PostFetchedEvent.builder()
                .influencerId(rawPosts.getInfluencer_id())
                .postId(rawPosts.getId())
                .platform(rawPosts.getPlatform())
                .timestamp(Instant.now())
                .build();
        kafkaTemplate.send(TOPIC_POST_FETCHED, event);
    }

//    public void publishInsightsFetchedEvent(RawInsights rawInsights){
//
//        PostFetchedEvent event = PostFetchedEvent.builder()
//                .influencerId(rawInsights.getInfluencer_id())
//                .postId(rawPosts.getId())
//                .platform(rawPosts.getPlatform())
//                .timestamp(Instant.now())
//                .build();
//        kafkaTemplate.send(TOPIC_POST_FETCHED, event);
//    }
}
