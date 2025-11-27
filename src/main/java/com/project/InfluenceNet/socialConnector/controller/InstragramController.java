package com.project.InfluenceNet.socialConnector.controller;

import com.project.InfluenceNet.influencer.entity.SocialAccount;
import com.project.InfluenceNet.socialConnector.client.InstagramClient;
import com.project.InfluenceNet.socialConnector.service.SocialPollingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/instagram")
@RequiredArgsConstructor
public class InstragramController {


    private final InstagramClient instagramClient;
    private final SocialPollingService socialPollingService;


    @GetMapping("/data")
    public Map<String, Object> getInstagramUserData() {
        return instagramClient.getInstagramUserData();
    }

    @GetMapping("/media")
    public Map<String, Object> getInstagramMediaData() {
        return instagramClient.getInstagramMediaData();
    }

    @GetMapping("/insights")
    public Map<String, Object> getInstagramInsightsData() {
        return instagramClient.getInstagramInsightsData();
    }

    @GetMapping("/instaAccounts")
    public List<SocialAccount> getInstaAccounts(){
        return socialPollingService.getAllInstagramAccounts();
    }
}
