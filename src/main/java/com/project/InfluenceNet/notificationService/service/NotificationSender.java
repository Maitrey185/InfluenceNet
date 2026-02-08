package com.project.InfluenceNet.notificationService.service;

import com.project.InfluenceNet.notificationService.model.NotificationRequest;
import com.project.InfluenceNet.notificationService.model.NotificationTemplate;
import org.springframework.stereotype.Service;

@Service
public interface NotificationSender {

    void send(NotificationRequest template);
}
