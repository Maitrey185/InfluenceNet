package com.project.InfluenceNet.socialConnector.service;

import com.project.InfluenceNet.influencer.dto.SocialAccountResponse;
import com.project.InfluenceNet.influencer.service.SocialAccountService;
import com.project.InfluenceNet.socialConnector.documents.Platform;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SocialPollingSchedulerService {

    private final SocialAccountService socialAccountService;
    private final InstagramConnectorOrchestrator instagramConnectorOrchestrator;

    public List<SocialAccountResponse> getAllInstagramAccounts(){
        return socialAccountService.getActiveSocialAccountsForPlatform(Platform.INSTAGRAM);
    }

    @Scheduled(fixedRate = 240000)  // 4 minutes
    public void scheduleProfileAndPostPolling() {

        List<SocialAccountResponse> accounts = getAllInstagramAccounts();

        for (SocialAccountResponse account : accounts) {
            instagramConnectorOrchestrator.syncInstagramProfile(account);
            instagramConnectorOrchestrator.syncInstagramMedia(account);
        }
    }


    @Scheduled(fixedRate = 60000)  // 1 minute
    public void scheduleInsightPolling() {

        List<SocialAccountResponse> accounts = getAllInstagramAccounts();

        for (SocialAccountResponse account : accounts) {
            instagramConnectorOrchestrator.syncInstagramInsights(account);
        }
    }


}
