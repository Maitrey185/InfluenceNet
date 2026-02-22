package com.project.InfluenceNet.analyticsService.service;

import com.project.InfluenceNet.analyticsService.dto.BestPostingTimeHeatmapResponse;
import com.project.InfluenceNet.analyticsService.dto.EngagementHeatmapCellProjection;
import com.project.InfluenceNet.analyticsService.dto.TopPostProjection;
import com.project.InfluenceNet.analyticsService.entity.InfluencerKPI;
import com.project.InfluenceNet.analyticsService.entity.PostAnalytics;
import com.project.InfluenceNet.analyticsService.exception.PostNotFoundException;
import com.project.InfluenceNet.analyticsService.repository.PostAnalyticsRepository;
import com.project.InfluenceNet.socialConnector.documents.Platform;
import com.project.InfluenceNet.socialConnector.documents.RawInsights;
import com.project.InfluenceNet.socialConnector.documents.RawPosts;
import com.project.InfluenceNet.socialConnector.repository.RawInsightsRepository;
import com.project.InfluenceNet.socialConnector.repository.RawPostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final PostAnalyticsRepository postAnalyticsRepository;

    private final RawPostsRepository rawPostsRepository;
    private final InfluencerKPIService influencerKPIService;
//    private final EngagementHeatmapService engagementHeatmapService;

    public void processRawInsight(RawInsights rawInsight) {

        RawPosts rawPost = rawPostsRepository.findById(rawInsight.getId())
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + rawInsight.getId()));


//        engagementHeatmapService.invalidateHeatmapCache(rawPost.getInfluencer_id(), rawPost.getPlatform());

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
        double engagementRate =
                newReach == 0 ? 0.0 :
                        (double)(newLikes+newComments+newShares+newSaves)/(double)newReach;
        pa.setEngagementRate(engagementRate);
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
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
        return postAnalyticsRepository.fetchTopPostsInAPeriod(influencerId, platform.name(), startDate, endDate, limit);
    }

    public BestPostingTimeHeatmapResponse fetchEngagementHeatmap(
            UUID influencerId,
            Platform platform,   // enum → string
            LocalDate startDate,
            LocalDate endDate
    ){

        List<EngagementHeatmapCellProjection> rows = postAnalyticsRepository.fetchEngagementHeatmap(influencerId, platform.name(), startDate, endDate);

        long[][] matrix = new long[7][24];
        for (EngagementHeatmapCellProjection row : rows) {

            int dayIndex  = row.getDayOfWeek() - 1; // 1–7 → 0–6
            int hourIndex = row.getHourOfDay();     // 0–23

            matrix[dayIndex][hourIndex] = row.getEngagement();
        }

        BestPostingTimeHeatmapResponse bestPostingTimeHeatmapResponse = BestPostingTimeHeatmapResponse.builder()
                .days(days())
                .hours(hours())
                .matrix(matrix)
                .build();

        return bestPostingTimeHeatmapResponse;

    }

    public static List<String> days() {
        return Arrays.stream(DayOfWeek.values())
                .map(d -> d.name().substring(0, 3)) // MON, TUE...
                .collect(Collectors.toList());
    }

    public static List<Integer> hours() {
        return IntStream.range(0, 24)
                .boxed()
                .collect(Collectors.toList());
    }


}
