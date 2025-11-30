package com.project.InfluenceNet.socialConnector.controller;

import com.project.InfluenceNet.influencer.dto.SocialAccountResponse;
import com.project.InfluenceNet.socialConnector.client.InstagramClient;
import com.project.InfluenceNet.socialConnector.dto.InstagramProfileDTO;
import com.project.InfluenceNet.socialConnector.service.InstagramConnectorOrchestrator;
import com.project.InfluenceNet.socialConnector.service.SocialPollingSchedulerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/instagram")
@RequiredArgsConstructor
public class InstragramController {


    private final InstagramClient instagramClient;
    private final SocialPollingSchedulerService socialPollingService;
    private final InstagramConnectorOrchestrator instagramConnectorOrchestrator;

    @GetMapping("/userProfile")
    public InstagramProfileDTO getInstagramUserData() {
        return instagramClient.getInstagramUserData("dummy");
    }

    @GetMapping("/media")
    public void getInstagramMediaData() {
//        return instagramClient.getInstagramMediaData("dummy", LocalDateTime.now().minusHours(6) );
//          instagramConnectorOrchestrator.syncInstagramMedia();
        socialPollingService.scheduleProfileAndPostPolling();
    }

    @GetMapping("/insights")
    public void getInstagramInsightsData() {
//        return instagramClient.getInstagramInsightsData("dummy", "dummy");
//        instagramConnectorOrchestrator.syncInstagramInsights();
        socialPollingService.scheduleInsightPolling();
    }

    @GetMapping("/instaAccounts")
    public List<SocialAccountResponse> getInstaAccounts(){
        return socialPollingService.getAllInstagramAccounts();
    }
}
