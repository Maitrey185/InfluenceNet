package com.project.InfluenceNet.collaborationNeo4jService.controller;

import com.project.InfluenceNet.collaborationNeo4jService.nodes.InfluencerNode;
import com.project.InfluenceNet.collaborationNeo4jService.nodes.PostNode;
import com.project.InfluenceNet.collaborationNeo4jService.service.PostNodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/PostNode")
public class PostNodeController {

    private final PostNodeService postNodeService;

    @PostMapping("/create/{influencerId}")
    public ResponseEntity<InfluencerNode> createPostNode(@PathVariable UUID influencerId, @RequestBody PostNode node){
        InfluencerNode influencerNode = postNodeService.saveOrUpdate(influencerId, node);
        return ResponseEntity.ok(influencerNode);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<PostNode> getPostNode(@PathVariable String id){
        PostNode postNode = postNodeService.getById(id);
        return ResponseEntity.ok(postNode);
    }

    @DeleteMapping("/delete/{id}")
    public void deletePostNdoe(@PathVariable String id){
        postNodeService.delete(id);
    }

}
