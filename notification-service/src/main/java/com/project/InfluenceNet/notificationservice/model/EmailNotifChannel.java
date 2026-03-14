package com.project.InfluenceNet.notificationservice.model;

import com.project.InfluenceNet.notificationservice.service.EmailNotificationSender;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailNotifChannel implements NotificationChannel{

    private final EmailNotificationSender emailNotificationSender;

    @Async("notificationExecutor")
    @Override
    public void send(NotificationRequest notificationRequest) {
        emailNotificationSender.send(notificationRequest);
    }

    @Override
    public String getType() {
        return "EMAIL";
    }
}
