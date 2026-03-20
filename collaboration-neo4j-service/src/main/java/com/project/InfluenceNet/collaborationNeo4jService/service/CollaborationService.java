package com.project.InfluenceNet.collaborationNeo4jService.service;

import com.project.InfluenceNet.collaborationNeo4jService.dto.CoPostProjection;
import com.project.InfluenceNet.collaborationNeo4jService.dto.MentionIntentProjection;
import com.project.InfluenceNet.collaborationNeo4jService.dto.MutualEngagementProjection;
import com.project.InfluenceNet.collaborationNeo4jService.nodes.InfluencerNode;
import com.project.InfluenceNet.collaborationNeo4jService.repository.InfluencerNodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CollaborationService {


        private final InfluencerNodeRepository repository;

        public void addMention(UUID fromId, UUID toId){
            repository.addMention(fromId, toId);
        }

        public void addCoPost(UUID id1, UUID id2){
            repository.addCoPost(id1, id2);
        }

        public void addEngagement(
                UUID influencerId,
                String postId,
                Integer likes,
                Integer comments
        ){
            repository.addEngagement(influencerId, postId, likes, comments);
        }

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

        public void deriveInterestEdges(int minScore){
            repository.deriveInterestEdges(minScore);
        }

        public void deriveSimilarGrowthEdges(){
            repository.deriveSimilarGrowthEdges();
        }

        public void deriveAudienceOverlap(){
            repository.deriveAudienceOverlap();
        }

        public void derivePotentialCollaborations(){
            repository.derivePotentialCollaborations();
        }

        public List<InfluencerNode> recommendedCollaborators(UUID id){
            return repository.recommendCollaborators(id);
        }



}
