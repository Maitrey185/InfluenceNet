package com.project.InfluenceNet.socialConnector.service;

import com.project.InfluenceNet.socialConnector.documents.Platform;
import com.project.InfluenceNet.socialConnector.documents.PostType;
import com.project.InfluenceNet.socialConnector.documents.RawInsights;
import com.project.InfluenceNet.socialConnector.documents.RawPosts;
import com.project.InfluenceNet.socialConnector.dto.InstagramRecentPostsDTO;
import com.project.InfluenceNet.socialConnector.dto.MediaInsightsDTO;
import com.project.InfluenceNet.socialConnector.events.PostAndInsightFetchedEventPublisher;
import com.project.InfluenceNet.socialConnector.exception.SocialIngestionException;
import com.project.InfluenceNet.socialConnector.repository.RawInsightsRepository;
import com.project.InfluenceNet.socialConnector.repository.RawPostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RawIngestionService {

    private final RawPostsRepository rawPostsRepository;
    private final RawInsightsRepository rawInsightsRepository;
    private final PostAndInsightFetchedEventPublisher postAndInsightFetchedEventPublisher;


    public void ingestRawPosts(List<InstagramRecentPostsDTO> posts, UUID influencerId){
        posts.forEach(post -> {
            try {
                RawPosts rawPosts = RawPosts.builder()
                        .id(post.getId())
                        .platform(Platform.INSTAGRAM)
                        .post_type(PostType.valueOf(post.getMedia_type()))
                        .media_url(post.getMedia_url())
                        .fetched_at(LocalDate.now())
                        .timestamp(LocalDateTime.now())
                        .influencer_id(influencerId)
                        .caption(post.getCaption())
                        .raw_payload(post)
                        .build();
                rawPostsRepository.save(rawPosts);
                postAndInsightFetchedEventPublisher.publishPostFetchedEvent(rawPosts);
            } catch (IllegalArgumentException e) {
                throw new SocialIngestionException("Unsupported/invalid post type from Instagram: " + post.getMedia_type(), e);
            } catch (Exception e) {
                throw new SocialIngestionException("Failed to ingest raw Instagram post with id: " + post.getId(), e);
            }

        });
    }

    public void ingestRawInsights(String postId, MediaInsightsDTO mediaInsightsDTO, UUID influenceId){
        try {
            RawInsights rawInsights = RawInsights.builder()
                    .id(postId)
                    .platform(Platform.INSTAGRAM)
                    .likes(mediaInsightsDTO.getLikes())
                    .shares(mediaInsightsDTO.getShares())
                    .comments(mediaInsightsDTO.getComments())
                    .saves(mediaInsightsDTO.getSaved())
                    .reach(mediaInsightsDTO.getReach())
                    .ig_reels_video_view_total_time(mediaInsightsDTO.getIg_reels_video_view_total_time())
                    .ig_reels_avg_watch_time(mediaInsightsDTO.getIg_reels_avg_watch_time())
                    .total_interactions(mediaInsightsDTO.getEngagement())
                    .views(mediaInsightsDTO.getViews())
                    .fetched_at(LocalDate.now())
                    .build();
            rawInsightsRepository.save(rawInsights);
            postAndInsightFetchedEventPublisher.publishInsightsFetchedEvent(rawInsights);
        } catch (Exception e) {
            throw new SocialIngestionException("Failed to ingest raw Instagram insights for post id: " + postId, e);
        }
    }
}
