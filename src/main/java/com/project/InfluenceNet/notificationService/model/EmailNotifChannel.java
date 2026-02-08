package com.project.InfluenceNet.notificationService.model;

import com.project.InfluenceNet.notificationService.service.EmailNotificationSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailNotifChannel implements NotificationChannel{

    private final EmailNotificationSender emailNotificationSender;

    @Override
    public void send(NotificationRequest notificationRequest) {
        emailNotificationSender.send(notificationRequest);
    }

    @Override
    public String getType() {
        return "EMAIL";
    }
}
