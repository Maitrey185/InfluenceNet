package com.project.InfluenceNet.analyticsService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;


@SpringBootApplication(scanBasePackages = {
        "com.project.InfluenceNet.analyticsService",
        "com.project.InfluenceNet.contracts.posts"
})
@EnableRetry
public class AnalyticsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnalyticsServiceApplication.class, args);
    }
}