package com.project.InfluenceNet.socialConnector.service;

import com.mongodb.annotations.Sealed;
import com.project.InfluenceNet.influencer.entity.SocialAccount;
import com.project.InfluenceNet.influencer.service.SocialAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SocialPollingService {

    private final SocialAccountService socialAccountService;
    public List<SocialAccount> getAllInstagramAccounts(){
        return socialAccountService.getActiveSocialAccountsForPlatform("Instagram");
    }
}
