package com.project.InfluenceNet.collaborationNeo4jService.controller;

import com.project.InfluenceNet.collaborationNeo4jService.nodes.NicheNode;
import com.project.InfluenceNet.collaborationNeo4jService.nodes.PostNode;
import com.project.InfluenceNet.collaborationNeo4jService.service.PostNodeService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/PostNode")
public class PostNodeController {

    private final PostNodeService postNodeService;

    @PostMapping("/create")
    public ResponseEntity<PostNode> createPostNode(@RequestBody PostNode node){
        PostNode postNode = postNodeService.saveOrUpdate(node);
        return ResponseEntity.ok(postNode);
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
