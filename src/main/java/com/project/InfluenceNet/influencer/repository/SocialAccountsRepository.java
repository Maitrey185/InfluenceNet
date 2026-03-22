package com.project.InfluenceNet.influencer.repository;

import com.project.InfluenceNet.influencer.entity.SocialAccount;
import com.project.InfluenceNet.contracts.InfluencerPostContract.Platform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SocialAccountsRepository extends JpaRepository<SocialAccount, UUID> {

    Optional<SocialAccount> findByInfluencerIdAndPlatform(UUID influencerId, Platform platform);

    List<SocialAccount> findByInfluencerId(UUID influencerId);

    boolean existsByInfluencerIdAndPlatform(UUID influencerId, Platform platform);

    List<SocialAccount> findByPlatformAndInfluencerIsActive(Platform platform, boolean isActive);
}
