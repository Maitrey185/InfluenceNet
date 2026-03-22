package com.project.InfluenceNet.socialConnector.repository;

import com.project.InfluenceNet.contracts.InfluencerPostContract.RawPosts;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RawPostsRepository extends MongoRepository<RawPosts, String> {
}
