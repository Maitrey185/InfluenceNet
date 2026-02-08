package com.project.InfluenceNet.notificationService.model;

public interface NotificationChannel {

    void send(NotificationRequest notificationRequest);

    String getType();
}
