package com.project.InfluenceNet.schedulerreminderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {
        "com.project.InfluenceNet.schedulerReminderService",
        "com.project.InfluenceNet.contracts.notification"
})
@EnableScheduling
public class SchedulerReminderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SchedulerReminderServiceApplication.class, args);
    }
}
