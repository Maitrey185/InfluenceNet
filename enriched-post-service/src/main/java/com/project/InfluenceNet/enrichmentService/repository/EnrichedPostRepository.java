package com.project.InfluenceNet.enrichmentService.repository;

import com.project.InfluenceNet.enrichmentService.document.EnrichedPost;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrichedPostRepository extends MongoRepository<EnrichedPost, String> {
}
