package com.project.InfluenceNet.collaborationNeo4jService.controller;

import com.project.InfluenceNet.collaborationNeo4jService.nodes.InfluencerNode;
import com.project.InfluenceNet.collaborationNeo4jService.service.InfluencerNicheRelationService;
import com.project.InfluenceNet.collaborationNeo4jService.service.InfluencerNodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class InfluencerNodeController {

    private final InfluencerNodeService influencerNodeService;
    private final InfluencerNicheRelationService influencerNicheRelationService;

    @PostMapping("/create")
    public ResponseEntity<InfluencerNode> createInfluencerNode(@RequestBody InfluencerNode node){
        InfluencerNode influencerNode = influencerNodeService.saveOrUpdate(node);
        return ResponseEntity.ok(influencerNode);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InfluencerNode> get(@PathVariable UUID id) {
        return ResponseEntity.ok(influencerNodeService.getById(id));
    }

    @GetMapping("/addNiche/{influencerId}")
    public ResponseEntity<InfluencerNode> addNiche(@PathVariable UUID influencerId,
                                                            @RequestParam List<String> nicheNames){
        return ResponseEntity.ok(influencerNicheRelationService.addNiches(influencerId, nicheNames));
    }

    @GetMapping("/removeNiche/{influencerId}")
    public ResponseEntity<InfluencerNode> removeNiche(@PathVariable UUID influencerId,
                                                   @RequestParam List<String> nicheNames){
        return ResponseEntity.ok(influencerNicheRelationService.removeNiches(influencerId, nicheNames));
    }




    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        influencerNodeService.delete(id);
    }
}
