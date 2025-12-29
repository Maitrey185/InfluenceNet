package com.project.InfluenceNet.collaborationNeo4jService.service;

import com.project.InfluenceNet.collaborationNeo4jService.nodes.InfluencerNicheRelation;
import com.project.InfluenceNet.collaborationNeo4jService.nodes.InfluencerNode;
import com.project.InfluenceNet.collaborationNeo4jService.nodes.NicheNode;
import com.project.InfluenceNet.collaborationNeo4jService.repository.InfluencerNodeRepository;

import com.project.InfluenceNet.collaborationNeo4jService.repository.NicheNodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InfluencerNicheService {

    private final InfluencerNodeRepository influencerRepo;
    private final NicheNodeRepository nicheNodeRepository;

    @Transactional(transactionManager = "neo4jTransactionManager")
    public InfluencerNode addNiches(
            UUID influencerId,
            List<String> nicheNames
    ) {
        InfluencerNode influencer = influencerRepo.findById(influencerId)
                .orElseThrow(() -> new RuntimeException("Influencer not found"));

        // Ensure niches set is initialized
        if (influencer.getNiches() == null) {
            influencer.setNiches(new HashSet<>());
        }

        // Fetch existing niches from DB
        List<NicheNode> existingNiches = nicheNodeRepository.findAllById(nicheNames);

        // Map for fast lookup
        Map<String, NicheNode> nicheMap = existingNiches.stream()
                .collect(Collectors.toMap(NicheNode::getName, Function.identity()));

        for (String nicheName : nicheNames) {

            NicheNode niche = nicheMap.getOrDefault(
                    nicheName,
                    NicheNode.builder().name(nicheName).build() // auto-created if missing
            );

            // Avoid duplicate relationships
            boolean alreadyLinked = influencer.getNiches().stream()
                    .anyMatch(r -> r.getNiche().getName().equals(nicheName));

            if (alreadyLinked) {
                continue;
            }

            InfluencerNicheRelation relation = InfluencerNicheRelation.builder()
                    .niche(niche)
                    .build();

            influencer.getNiches().add(relation);
        }

        return influencerRepo.save(influencer);
    }

    @Transactional(transactionManager = "neo4jTransactionManager")
    public InfluencerNode removeNiches(
            UUID influencerId,
            List<String> nicheNames
    ) {
        InfluencerNode influencer = influencerRepo.findById(influencerId)
                .orElseThrow(() -> new RuntimeException("Influencer not found"));


        if (influencer.getNiches() == null || influencer.getNiches().isEmpty()) {
            return influencer;
        }

        // Convert to Set for fast lookup
        Set<String> nicheNameSet = new HashSet<>(nicheNames);

        // REMOVE relationships whose niche name matches
        influencer.getNiches().removeIf(relation ->
                nicheNameSet.contains(relation.getNiche().getName())
        );

        return influencerRepo.save(influencer);
    }


}
