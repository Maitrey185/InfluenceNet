package com.project.InfluenceNet.collaborationNeo4jService.controller;

import com.project.InfluenceNet.collaborationNeo4jService.dto.CoPostProjection;
import com.project.InfluenceNet.collaborationNeo4jService.dto.MentionIntentProjection;
import com.project.InfluenceNet.collaborationNeo4jService.dto.MutualEngagementProjection;
import com.project.InfluenceNet.collaborationNeo4jService.nodes.InfluencerNode;
import com.project.InfluenceNet.collaborationNeo4jService.service.CollaborationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/collaboration")
@RequiredArgsConstructor
public class CollaborationController {

    private final CollaborationService service;

    @PostMapping("/add-mention")
    public void addMention(UUID fromId, UUID toId){
        service.addMention(fromId, toId);
    }

    @PostMapping("/add-copost")
    public void addCoPost(UUID id1, UUID id2){
        service.addCoPost(id1, id2);
    }

    @PostMapping("/add-engagement")
    void addEngagement(
            UUID influencerId,
            String postId,
            Integer likes,
            Integer comments
    ){
        service.addEngagement(influencerId, postId, likes, comments);
    }

    @GetMapping("/mutual-engagements")
    public List<MutualEngagementProjection> mutualEngagements() {
        return service.getMutualEngagements();
    }

    @GetMapping("/mention-intent")
    public List<MentionIntentProjection> mentionIntent(
            @RequestParam(defaultValue = "3") int minMentions) {
        return service.getMentionIntents(minMentions);
    }

    @GetMapping("/strong-collabs")
    public List<CoPostProjection> strongCollabs(
            @RequestParam(defaultValue = "2") int minTimes) {
        return service.getStrongCollaborations(minTimes);
    }

    @GetMapping("/derive-interestEdges")
    public void deriveInterestEdges(int minScore){
        service.deriveInterestEdges(minScore);
    }

    @GetMapping("/derive-similarGrowthEdges")
    public void deriveSimilarGrowthEdges(){
        service.deriveSimilarGrowthEdges();
    }

    @GetMapping("/derive-audienceOverlapEdges")
    public void deriveAudienceOverlap(){
        service.deriveAudienceOverlap();
    }

    @GetMapping("/derive-potentialCollabEdges")
    public void derivePotentialCollaborations(){
        service.derivePotentialCollaborations();
    }

    @GetMapping("/derive-recommendedCollaborators/{id}")
    public List<InfluencerNode> deriveRecommendedCollaborators(@PathVariable UUID id){
        return service.recommendedCollaborators(id);
    }
}
