package com.project.InfluenceNet.notificationService.service;

import com.project.InfluenceNet.notificationService.model.NotificationRequest;
import com.project.InfluenceNet.notificationService.model.NotificationTemplate;
import org.springframework.stereotype.Service;

@Service
public class PushNotificationSender implements NotificationSender{

    @Override
    public void send(NotificationRequest template) {

    }
}
