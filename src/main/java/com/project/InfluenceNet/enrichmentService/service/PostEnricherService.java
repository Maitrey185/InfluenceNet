package com.project.InfluenceNet.enrichmentService.service;

import com.project.InfluenceNet.enrichmentService.document.EnrichedPost;
import com.project.InfluenceNet.enrichmentService.repository.EnrichedPostRepository;
import com.project.InfluenceNet.socialConnector.documents.RawPosts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostEnricherService {

    private final PostEnrichmentHelper postEnrichmentHelper;
    private final EnrichedPostRepository enrichedPostRepository;

    public void enrichPostAndStore(RawPosts rawPosts) {

        List<String> captions = postEnrichmentHelper.extractHashtags(rawPosts.getCaption());
        List<String> mentions = postEnrichmentHelper.extractMentions(rawPosts.getCaption());

        EnrichedPost.Enrichments enrichments = EnrichedPost.Enrichments.builder()
                .hashtags(captions)
                .mentions(mentions)
                .language(postEnrichmentHelper.detectLanguage(rawPosts.getCaption()))
                .postType(rawPosts.getPost_type())
                .build();

        EnrichedPost enrichedPost = EnrichedPost.builder()
                .postId(rawPosts.getId())
                .influencerId(rawPosts.getInfluencer_id())
                .platform(rawPosts.getPlatform())
                .enrichments(enrichments)
                .enrichedAt(Instant.now())
                .build();

        enrichedPostRepository.save(enrichedPost);
    }

}
