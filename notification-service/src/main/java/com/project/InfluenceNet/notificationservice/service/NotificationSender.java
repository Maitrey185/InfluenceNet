package com.project.InfluenceNet.notificationservice.service;

import com.project.InfluenceNet.notificationservice.model.NotificationRequest;
import org.springframework.stereotype.Service;

@Service
public interface NotificationSender {

    void send(NotificationRequest template);
}
