package com.project.InfluenceNet.collaborationNeo4jService.controller;

import com.project.InfluenceNet.collaborationNeo4jService.nodes.InfluencerNode;
import com.project.InfluenceNet.collaborationNeo4jService.service.InfluencerNodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class InfluencerNodeController {

    private final InfluencerNodeService influencerNodeService;

    @PostMapping("/create")
    public ResponseEntity<InfluencerNode> createInfluencerNode(@RequestBody InfluencerNode node){
        InfluencerNode influencerNode = influencerNodeService.createInfluencerNode(node);
        return ResponseEntity.ok(influencerNode);
    }
}
