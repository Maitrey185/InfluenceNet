package com.project.InfluenceNet.collaborationNeo4jService.service;

import com.project.InfluenceNet.collaborationNeo4jService.dto.CoPostProjection;
import com.project.InfluenceNet.collaborationNeo4jService.dto.MentionIntentProjection;
import com.project.InfluenceNet.collaborationNeo4jService.dto.MutualEngagementProjection;
import com.project.InfluenceNet.collaborationNeo4jService.repository.InfluencerNodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CollaborationService {


        private final InfluencerNodeRepository repository;

        /* Mutual engagement */
        public List<MutualEngagementProjection> getMutualEngagements() {
            return repository.findMutualEngagements();
        }

        /* Influencers who are clearly interested */
        public List<MentionIntentProjection> getMentionIntents(int minMentions) {
            return repository.findMentionIntent(minMentions);
        }

        /* Proven collaboration */
        public List<CoPostProjection> getStrongCollaborations(int minTimes) {
            return repository.findStrongCollaborations(minTimes);
        }



}
