package com.project.InfluenceNet.notificationService.model;

import org.springframework.stereotype.Component;

@Component
public class EmailNotifChannel implements NotificationChannel{


    @Override
    public void send(NotificationRequest notificationRequest) {
        System.out.println("Email");
    }

    @Override
    public String getType() {
        return "EMAIL";
    }
}
