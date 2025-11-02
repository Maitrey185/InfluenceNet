package com.project.InfluenceNet.influencer.repository;

import com.project.InfluenceNet.influencer.entity.InfluencerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface InfluencerProfileRepository extends JpaRepository<InfluencerProfile, UUID> {

    Optional<InfluencerProfile> findById(UUID userId);

    Optional<InfluencerProfile> findByUsername(String username);

    Optional<InfluencerProfile> findByEmail(String email);

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);


}
