package com.project.InfluenceNet.enrichmentService.event;

import com.project.InfluenceNet.enrichmentService.exception.PostNotFoundException;
import com.project.InfluenceNet.enrichmentService.service.PostEnricherService;
import com.project.InfluenceNet.contracts.posts.RawPosts;
import com.project.InfluenceNet.socialConnector.events.PostFetchedEvent;
import com.project.InfluenceNet.socialConnector.repository.RawPostsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostFetchedSubscriber {

    private final RawPostsRepository rawPostsRepository;
    private final PostEnricherService postEnricherService;

    @RetryableTopic(
            attempts = "3",
            backoff = @Backoff(delay = 2000),
            autoCreateTopics = "true"
    )
    @KafkaListener(topics = "post.fetched", groupId = "post.fetched.group")
    public void handlePostFetchedEvent(PostFetchedEvent event) {
        log.info("Post fetched event received: {}", event);

        RawPosts rawPosts = rawPostsRepository.findById(event.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + event.getPostId()));

        postEnricherService.enrichPostAndStore(rawPosts);
    }
}
