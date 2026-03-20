package com.project.InfluenceNet.socialConnector.controller;

import com.project.InfluenceNet.contracts.InfluencerPostContract.RawInsights;
import com.project.InfluenceNet.socialConnector.client.InstagramClient;
import com.project.InfluenceNet.contracts.InfluencerPostContract.RawPosts;
import com.project.InfluenceNet.socialConnector.dto.InstagramProfileDTO;
import com.project.InfluenceNet.socialConnector.events.PostAndInsightFetchedEventPublisher;
import com.project.InfluenceNet.socialConnector.service.InstagramService;
import com.project.InfluenceNet.socialConnector.service.SocialPollingSchedulerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/instagram")
@RequiredArgsConstructor
public class InstagramController {


    private final InstagramClient instagramClient;
    private final SocialPollingSchedulerService socialPollingService;
    private final PostAndInsightFetchedEventPublisher postAndInsightFetchedEventPublisher;
    private final InstagramService instagramService;

    @GetMapping("/userProfile")
    public InstagramProfileDTO getInstagramUserData() {
        return instagramClient.getInstagramUserData("dummy");
    }

    @GetMapping("/media")
    public void getInstagramMediaData() {
//        instagramClient.getInstagramMediaData("dummy", LocalDateTime.now().minusHours(6) );
//          instagramConnectorOrchestrator.syncInstagramMedia();
        socialPollingService.scheduleProfileAndPostPolling();
    }

    @GetMapping("/insights")
    public void getInstagramInsightsData() {
//        instagramClient.getInstagramInsightsData("dummy", "dummy");
//        instagramConnectorOrchestrator.syncInstagramInsights();
        socialPollingService.scheduleInsightPolling();
    }

    @PostMapping("/publishPostFetchedEvent")
    public void publishPostFetchedEvent(@RequestBody RawPosts rawPost){
        postAndInsightFetchedEventPublisher.publishPostFetchedEvent(rawPost);
    }

    @GetMapping("/rawPosts/{id}")
    public RawPosts getRawPostsById(@PathVariable String id){
        return instagramService.getRawPostsById(id);
    }

    @GetMapping("/rawInsights/{id}")
    public RawInsights getRawInsightsById(@PathVariable String id){
        return instagramService.getRawInsightsById(id);
    }
}
