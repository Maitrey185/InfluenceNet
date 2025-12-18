package com.project.InfluenceNet.enrichmentService.event;

import com.project.InfluenceNet.enrichmentService.service.PostEnricherService;
import com.project.InfluenceNet.socialConnector.documents.RawPosts;
import com.project.InfluenceNet.socialConnector.events.PostFetchedEvent;
import com.project.InfluenceNet.socialConnector.repository.RawPostsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostFetchedSubscriber {

    private final RawPostsRepository rawPostsRepository;
    private final PostEnricherService postEnricherService;

//    @KafkaListener(topics = "post.fetched", groupId = "post.fetched.group")
//    public void handlePostFetchedEvent(PostFetchedEvent event) {
//        log.info("Post fetched event received: {}", event);
//
//        RawPosts rawPosts = rawPostsRepository.findById(event.getPostId())
//                .orElseThrow(() -> new RuntimeException("Post not found with id: " + event.getPostId()));
//
//        postEnricherService.enrichPostAndStore(rawPosts);
//    }
}
