package com.project.InfluenceNet.collaborationNeo4jService.controller;

import com.project.InfluenceNet.collaborationNeo4jService.dto.CoPostProjection;
import com.project.InfluenceNet.collaborationNeo4jService.dto.MentionIntentProjection;
import com.project.InfluenceNet.collaborationNeo4jService.dto.MutualEngagementProjection;
import com.project.InfluenceNet.collaborationNeo4jService.service.CollaborationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/collaboration")
@RequiredArgsConstructor
public class CollaborationController {

    private final CollaborationService service;

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
}
