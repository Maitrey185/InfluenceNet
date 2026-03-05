package com.project.InfluenceNet.socialConnector.service;

import com.project.InfluenceNet.influencer.dto.SocialAccountResponse;
import com.project.InfluenceNet.influencer.service.SocialAccountService;
import com.project.InfluenceNet.socialConnector.documents.Platform;
import com.project.InfluenceNet.socialConnector.exception.InstagramConnectorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
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
            try {
                instagramConnectorOrchestrator.syncInstagramProfile(account);
            } catch (InstagramConnectorException ex) {
                log.error("Scheduled Instagram profile sync failed. influencerId={}, platformUserId={}, errorCode={}, status={} message={}",
                        account.getInfluencerId(),
                        account.getPlatformUserId(),
                        ex.getErrorCode(),
                        ex.getStatus(),
                        ex.getMessage(),
                        ex);
            } catch (Exception ex) {
                log.error("Scheduled Instagram profile sync failed (unexpected). influencerId={}, platformUserId={} message={}",
                        account.getInfluencerId(),
                        account.getPlatformUserId(),
                        ex.getMessage(),
                        ex);
            }

            try {
                instagramConnectorOrchestrator.syncInstagramMedia(account);
            } catch (InstagramConnectorException ex) {
                log.error("Scheduled Instagram media sync failed. influencerId={}, platformUserId={}, errorCode={}, status={} message={}",
                        account.getInfluencerId(),
                        account.getPlatformUserId(),
                        ex.getErrorCode(),
                        ex.getStatus(),
                        ex.getMessage(),
                        ex);
            } catch (Exception ex) {
                log.error("Scheduled Instagram media sync failed (unexpected). influencerId={}, platformUserId={} message={}",
                        account.getInfluencerId(),
                        account.getPlatformUserId(),
                        ex.getMessage(),
                        ex);
            }
        }
    }


    @Scheduled(fixedRate = 60000)  // 1 minute
    public void scheduleInsightPolling() {

        List<SocialAccountResponse> accounts = getAllInstagramAccounts();

        for (SocialAccountResponse account : accounts) {
            try {
                instagramConnectorOrchestrator.syncInstagramInsights(account);
            } catch (InstagramConnectorException ex) {
                log.error("Scheduled Instagram insights sync failed. influencerId={}, platformUserId={}, errorCode={}, status={} message={}",
                        account.getInfluencerId(),
                        account.getPlatformUserId(),
                        ex.getErrorCode(),
                        ex.getStatus(),
                        ex.getMessage(),
                        ex);
            } catch (Exception ex) {
                log.error("Scheduled Instagram insights sync failed (unexpected). influencerId={}, platformUserId={} message={}",
                        account.getInfluencerId(),
                        account.getPlatformUserId(),
                        ex.getMessage(),
                        ex);
            }
        }
    }


}
