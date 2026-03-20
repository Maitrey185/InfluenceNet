package com.project.InfluenceNet.collaborationNeo4jService.controller;

import com.project.InfluenceNet.collaborationNeo4jService.nodes.NicheNode;
import com.project.InfluenceNet.collaborationNeo4jService.service.NicheNodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/niche")
public class NicheNodeController {

    private final NicheNodeService nicheNodeService;

    @PostMapping("/create")
    public ResponseEntity<NicheNode> createNicheNode(@RequestBody NicheNode node){
        NicheNode nicheNode = nicheNodeService.saveOrUpdate(node);
        return ResponseEntity.ok(nicheNode);
    }

}
