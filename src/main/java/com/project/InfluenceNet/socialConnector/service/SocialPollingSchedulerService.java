package com.project.InfluenceNet.socialConnector.service;

import com.project.InfluenceNet.influencer.dto.SocialAccountResponse;
import com.project.InfluenceNet.influencer.service.SocialAccountService;
import com.project.InfluenceNet.socialConnector.documents.Platform;
import com.project.InfluenceNet.socialConnector.exception.InstagramConnectorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocialPollingSchedulerService {

    private final SocialAccountService socialAccountService;
    private final InstagramConnectorOrchestrator instagramConnectorOrchestrator;

    @Qualifier("instagramPollingExecutor")
    private final Executor instagramPollingExecutor;

    public List<SocialAccountResponse> getAllInstagramAccounts(){
        return socialAccountService.getActiveSocialAccountsForPlatform(Platform.INSTAGRAM);
    }

    @Scheduled(fixedRate = 240000)
    public void scheduleProfileAndPostPolling() {

        List<SocialAccountResponse> accounts = getAllInstagramAccounts();

        List<CompletableFuture<Void>> futures = accounts.stream()
                .map(account ->
                        CompletableFuture.runAsync(() -> processAccount(account), instagramPollingExecutor)
                )
                .toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }


    @Scheduled(fixedRate = 60000)
    public void scheduleInsightPolling() {

        List<SocialAccountResponse> accounts = getAllInstagramAccounts();

        List<CompletableFuture<Void>> futures = accounts.stream()
                .map(account ->
                        CompletableFuture.runAsync(() -> processInsights(account), instagramPollingExecutor)
                )
                .toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    private void processAccount(SocialAccountResponse account) {

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

    private void processInsights(SocialAccountResponse account) {

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
