package com.project.InfluenceNet.notificationservice.service;

import com.project.InfluenceNet.notificationservice.model.NotificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PushNotificationSender implements NotificationSender{

    @Override
    public void send(NotificationRequest template) {
        log.info("Push notification sent");
    }
}
