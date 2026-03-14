package com.project.InfluenceNet.notificationservice.model;

public interface NotificationChannel {

    void send(NotificationRequest notificationRequest);

    String getType();
}
