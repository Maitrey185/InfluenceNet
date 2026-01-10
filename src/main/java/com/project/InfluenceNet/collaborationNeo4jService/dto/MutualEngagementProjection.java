package com.project.InfluenceNet.collaborationNeo4jService.dto;

import com.project.InfluenceNet.collaborationNeo4jService.nodes.InfluencerNode;

public interface MutualEngagementProjection {

    Engagement getEngagement();

    interface Engagement {
        InfluencerNode getSource();
        InfluencerNode getTarget();
        Long getSharedPosts();
    }
}

