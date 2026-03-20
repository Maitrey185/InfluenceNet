package com.project.InfluenceNet.collaborationNeo4jService.dto;

import com.project.InfluenceNet.collaborationNeo4jService.nodes.InfluencerNode;

public interface MentionIntentProjection {

    InfluencerNode getFrom();
    InfluencerNode getTo();

    Integer getMentionCount();
}

