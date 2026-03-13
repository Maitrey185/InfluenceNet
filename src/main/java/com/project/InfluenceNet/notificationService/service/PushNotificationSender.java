package com.project.InfluenceNet.notificationService.service;

import com.project.InfluenceNet.notificationService.model.NotificationRequest;
import com.project.InfluenceNet.notificationService.model.NotificationTemplate;
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
