package com.project.InfluenceNet.enrichmentService.service;

import com.project.InfluenceNet.socialConnector.documents.RawPosts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostEnricherService {

    private final PostEnrichmentHelper postEnrichmentHelper;

    public void enrichPostAndStore(RawPosts rawPosts) {

        List<String> captions = postEnrichmentHelper.extractHashtags(rawPosts.getCaption());
        List<String> mentions = postEnrichmentHelper.extractMentions(rawPosts.getCaption());

    }

}
