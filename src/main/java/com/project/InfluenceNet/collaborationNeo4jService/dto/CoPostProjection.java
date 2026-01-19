package com.project.InfluenceNet.collaborationNeo4jService.dto;

import com.project.InfluenceNet.collaborationNeo4jService.nodes.InfluencerNode;

public interface CoPostProjection {

    InfluencerNode getA();
    InfluencerNode getB();

    Integer getTimes();
}

