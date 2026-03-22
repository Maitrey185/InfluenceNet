package com.project.InfluenceNet.socialConnector.client;

import com.project.InfluenceNet.contracts.InfluencerPostContract.InfluencerProfileResponse;
import com.project.InfluenceNet.contracts.InfluencerPostContract.RawPosts;
import com.project.InfluenceNet.contracts.InfluencerPostContract.SocialAccountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InfluencerServiceClient {

    private final RestClient restClient;

    public List<SocialAccountResponse> getActiveSocialAccounts() {
        return restClient.get()
                .uri("/socialAccount/active/INSTAGRAM")
                .retrieve()
                .body(new ParameterizedTypeReference<List<SocialAccountResponse>>() {});
    }

    public InfluencerProfileResponse updateFollowerCount(UUID id, Integer count){
        return restClient.put()
                .uri("/influencer/updateFollowerCount/{id}", id)
                .body(count)
                .retrieve()
                .body(InfluencerProfileResponse.class);
    }

    public RawPosts getRawPost(String id){
        return restClient.get()
                .uri("/instagram/rawPosts/{id}", id)
                .retrieve()
                .body(RawPosts.class);
    }


}
