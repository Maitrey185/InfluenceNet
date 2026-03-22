package com.project.InfluenceNet.enrichmentService.event;

import com.project.InfluenceNet.contracts.InfluencerPostContract.PostFetchedEvent;
import com.project.InfluenceNet.enrichmentService.exception.PostNotFoundException;
import com.project.InfluenceNet.enrichmentService.service.PostEnricherService;
import com.project.InfluenceNet.contracts.InfluencerPostContract.RawPosts;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostFetchedSubscriber {

    private final RestTemplate restTemplate;
    private final PostEnricherService postEnricherService;

    @RetryableTopic(
            attempts = "3",
            backoff = @Backoff(delay = 2000),
            autoCreateTopics = "true"
    )
    @KafkaListener(topics = "post.fetched", groupId = "post.fetched.group")
    public void handlePostFetchedEvent(PostFetchedEvent event) {
        log.info("Post fetched event received: {}", event);

        String socialConnectorUrl = "http://localhost:8094/instagram/rawPosts/" + event.getPostId();
        RawPosts rawPosts = restTemplate.getForObject(socialConnectorUrl, RawPosts.class);

        postEnricherService.enrichPostAndStore(rawPosts);
    }
}
