package com.project.InfluenceNet.socialConnector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication(scanBasePackages = {
        "com.project.InfluenceNet.socialConnector",
        "com.project.InfluenceNet.contracts.InfluencerPostContract"
})
@EnableRetry
public class SocialConnectorApplication {

    public static void main(String[] args) {
        SpringApplication.run(SocialConnectorApplication.class, args);
    }
}