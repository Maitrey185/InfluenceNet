package com.project.InfluenceNet.analyticsService.service;

import com.project.InfluenceNet.analyticsService.entity.InfluencerKPI;
import com.project.InfluenceNet.analyticsService.repository.InfluencerKPIRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InfluencerKPIService {

    private final InfluencerKPIRepository influencerKPIRepository;

    public void calculateAndStoreKPIs() {
        InfluencerKPI influencerKPI = InfluencerKPI.builder()
                .id(UUID.randomUUID())

                .build();
    }

}
