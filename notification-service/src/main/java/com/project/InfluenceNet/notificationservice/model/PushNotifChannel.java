package com.project.InfluenceNet.notificationservice.model;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class PushNotifChannel implements NotificationChannel{

    @Async("notificationExecutor")
    @Override
    public void send(NotificationRequest notificationRequest) {
        System.out.println("Push");
    }

    @Override
    public String getType() {
        return "PUSH";
    }
}
