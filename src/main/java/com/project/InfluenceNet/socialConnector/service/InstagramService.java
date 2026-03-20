package com.project.InfluenceNet.socialConnector.service;

import com.project.InfluenceNet.contracts.InfluencerPostContract.RawInsights;
import com.project.InfluenceNet.contracts.InfluencerPostContract.RawPosts;
import com.project.InfluenceNet.socialConnector.repository.RawInsightsRepository;
import com.project.InfluenceNet.socialConnector.repository.RawPostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InstagramService {

    public final RawPostsRepository rawPostsRepository;
    public final RawInsightsRepository rawInsightsRepository;

    public RawPosts getRawPostsById(String id){
        return rawPostsRepository.findById(id).orElse(null);
    }

    public RawInsights getRawInsightsById(String id){
        return rawInsightsRepository.findById(id).orElse(null);
    }

}
