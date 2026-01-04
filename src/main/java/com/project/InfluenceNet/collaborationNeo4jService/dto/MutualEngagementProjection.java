package com.project.InfluenceNet.collaborationNeo4jService.dto;

import com.project.InfluenceNet.collaborationNeo4jService.nodes.InfluencerNode;

public interface MutualEngagementProjection {

    InfluencerNode getA();
    InfluencerNode getB();

    Long getSharedPosts();
}

