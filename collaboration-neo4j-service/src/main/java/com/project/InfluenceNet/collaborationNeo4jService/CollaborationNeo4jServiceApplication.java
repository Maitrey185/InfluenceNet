package com.project.InfluenceNet.collaborationNeo4jService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication(scanBasePackages = {
        "com.project.InfluenceNet.collaborationNeo4jService",
        "com.project.InfluenceNet.contracts.InfluencerPostContract"
})
@EnableRetry
public class CollaborationNeo4jServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CollaborationNeo4jServiceApplication.class, args);
    }
}
