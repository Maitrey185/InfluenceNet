package com.project.InfluenceNet.notificationService.model;

import org.springframework.stereotype.Component;

@Component
public class PushNotifChannel implements NotificationChannel{
    @Override
    public void send(NotificationRequest notificationRequest) {
        System.out.println("Push");
    }

    @Override
    public String getType() {
        return "PUSH";
    }
}
