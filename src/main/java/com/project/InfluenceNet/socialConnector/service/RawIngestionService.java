package com.project.InfluenceNet.socialConnector.service;

import com.project.InfluenceNet.socialConnector.documents.Platforms;
import com.project.InfluenceNet.socialConnector.documents.PostType;
import com.project.InfluenceNet.socialConnector.documents.RawPosts;
import com.project.InfluenceNet.socialConnector.dto.InstagramRecentPostsDTO;
import com.project.InfluenceNet.socialConnector.repository.RawInsightsRepository;
import com.project.InfluenceNet.socialConnector.repository.RawPostsRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
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


    public void ingestRawPosts(List<InstagramRecentPostsDTO> posts, UUID influencerId){
        posts.forEach(post -> {
            try {
                RawPosts rawPosts = RawPosts.builder()
                        .id(post.getId())
                        .platform(Platforms.INSTAGRAM)
//                        .post_type(post.getMedia_type())
                        .media_url(post.getMedia_url())
                        .fetched_at(LocalDate.now())
                        .timestamp(LocalDateTime.now())
                        .influencer_id(influencerId)
                        .caption(post.getCaption())
                        .raw_payload(post)
                        .build();
                rawPostsRepository.save(rawPosts);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        });
    }
}
