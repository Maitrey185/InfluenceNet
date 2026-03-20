package com.project.InfluenceNet.socialConnector.repository;

import com.project.InfluenceNet.contracts.InfluencerPostContract.RawInsights;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RawInsightsRepository extends MongoRepository<RawInsights, String> {
}
