package com.project.InfluenceNet.socialConnector.controller;

import com.project.InfluenceNet.influencer.dto.SocialAccountResponse;
import com.project.InfluenceNet.influencer.entity.SocialAccount;
import com.project.InfluenceNet.socialConnector.client.InstagramClient;
import com.project.InfluenceNet.socialConnector.dto.InstagramProfileDTO;
import com.project.InfluenceNet.socialConnector.dto.InstagramRecentPostsDTO;
import com.project.InfluenceNet.socialConnector.dto.MediaInsightsResponse;
import com.project.InfluenceNet.socialConnector.service.InstagramConnector;
import com.project.InfluenceNet.socialConnector.service.InstagramConnectorOrchestrator;
import com.project.InfluenceNet.socialConnector.service.SocialPollingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/instagram")
@RequiredArgsConstructor
public class InstragramController {


    private final InstagramClient instagramClient;
    private final SocialPollingService socialPollingService;
    private final InstagramConnectorOrchestrator instagramConnectorOrchestrator;

    @GetMapping("/userProfile")
    public InstagramProfileDTO getInstagramUserData() {
        return instagramClient.getInstagramUserData("dummy");
    }

    @GetMapping("/media")
    public void getInstagramMediaData() {
//        return instagramClient.getInstagramMediaData("dummy", LocalDateTime.now().minusHours(6) );
          instagramConnectorOrchestrator.syncInstagramMedia();
    }

    @GetMapping("/insights")
    public MediaInsightsResponse getInstagramInsightsData() {
        return instagramClient.getInstagramInsightsData("dummy", "dummy");
    }

    @GetMapping("/instaAccounts")
    public List<SocialAccountResponse> getInstaAccounts(){
        return socialPollingService.getAllInstagramAccounts();
    }
}
