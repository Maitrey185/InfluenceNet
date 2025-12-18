package com.project.InfluenceNet.analyticsService.service;

import com.project.InfluenceNet.analyticsService.dto.EngagementHeatmapCellProjection;
import com.project.InfluenceNet.analyticsService.dto.TopPostProjection;
import com.project.InfluenceNet.analyticsService.entity.InfluencerKPI;
import com.project.InfluenceNet.analyticsService.entity.PostAnalytics;
import com.project.InfluenceNet.analyticsService.repository.PostAnalyticsRepository;
import com.project.InfluenceNet.socialConnector.documents.Platform;
import com.project.InfluenceNet.socialConnector.documents.RawInsights;
import com.project.InfluenceNet.socialConnector.documents.RawPosts;
import com.project.InfluenceNet.socialConnector.repository.RawInsightsRepository;
import com.project.InfluenceNet.socialConnector.repository.RawPostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final PostAnalyticsRepository postAnalyticsRepository;

    private final RawPostsRepository rawPostsRepository;
    private final InfluencerKPIService influencerKPIService;

    public void processRawInsight(RawInsights rawInsight) {

        RawPosts rawPost = rawPostsRepository.findById(rawInsight.getId())
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + rawInsight.getId()));


        int newLikes      = rawInsight.getLikes();
        int newComments   = rawInsight.getComments();
        int newShares     = rawInsight.getShares();
        int newSaves      = rawInsight.getSaves();
        int newReach      = rawInsight.getReach();
        int newViews      = rawInsight.getViews();

        PostAnalytics pa = fetchOldPostAnalytics(rawInsight, rawPost);

        int oldLikes       = pa.getLikes();
        int oldComments    = pa.getComments();
        int oldShares      = pa.getShares();
        int oldSaves       = pa.getSaves();
        int oldReach       = pa.getReach();
        int oldViews       = pa.getViews();

        int likesDiff      = newLikes - oldLikes;
        int commentsDiff   = newComments - oldComments;
        int sharesDiff     = newShares - oldShares;
        int savesDiff      = newSaves - oldSaves;
        int reachDiff      = newReach - oldReach;
        int viewsDiff      = newViews - oldViews;

        int newPost=0;
        if(oldLikes==0&&oldComments==0&&oldShares==0&&oldSaves==0&&oldReach==0&&oldViews==0){
            newPost=1;
        }

        pa.setLikes(newLikes);
        pa.setComments(newComments);
        pa.setShares(newShares);
        pa.setSaves(newSaves);
        pa.setReach(newReach);
        pa.setViews(newViews);
        pa.setEngagementRate((double)(newLikes+newComments+newShares+newSaves)/(double)newReach);
        pa.setUpdatedAt(LocalDateTime.now());
        postAnalyticsRepository.save(pa);

        influencerKPIService.calculateAndStoreKPIs(newPost, rawPost.getInfluencer_id(), rawInsight.getPlatform(), rawInsight.getFetched_at(), likesDiff, commentsDiff, sharesDiff, savesDiff, reachDiff, viewsDiff);

    }

    public PostAnalytics fetchOldPostAnalytics(RawInsights rawInsights, RawPosts rawPosts){

            PostAnalytics postAnalytics = postAnalyticsRepository.findById(rawInsights.getId())
                    .orElseGet(()->PostAnalytics.newForPost(rawInsights.getId(), rawPosts.getInfluencer_id(), rawInsights.getPlatform(), rawPosts.getPost_type(), rawPosts.getTimestamp()));

            return postAnalytics;
    }

    public List<TopPostProjection> fetchTopPost(UUID influencerId,
                                                Platform platform,
                                                LocalDate startDate,
                                                LocalDate endDate,
                                                int limit){
        return postAnalyticsRepository.fetchTopPostsInAPeriod(influencerId, platform.name(), startDate, endDate, limit);
    }

    public List<EngagementHeatmapCellProjection> fetchEngagementHeatmap(
            UUID influencerId,
            Platform platform,   // enum → string
            LocalDate startDate,
            LocalDate endDate
    ){
        return postAnalyticsRepository.fetchEngagementHeatmap(influencerId, platform.name(), startDate, endDate);
    }


}
