package com.project.InfluenceNet.enrichmentService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;


@SpringBootApplication(scanBasePackages = {
        "com.project.InfluenceNet.enrichmentService",
        "com.project.InfluenceNet.contracts.InfluencerPostContract"
})
@EnableRetry
public class EnrichPostServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnrichPostServiceApplication.class, args);
    }
}