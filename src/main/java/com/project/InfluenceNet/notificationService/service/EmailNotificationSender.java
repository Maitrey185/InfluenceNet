package com.project.InfluenceNet.notificationService.service;

import com.project.InfluenceNet.notificationService.model.NotificationRequest;
import com.project.InfluenceNet.notificationService.model.NotificationTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailNotificationSender implements NotificationSender{

    private final EmailService emailService;

    @Override
    public void send(NotificationRequest notificationRequest) {
        log.info("Sending email notification: {}", notificationRequest);
        emailService.sendMail(notificationRequest.getEmail(), notificationRequest.getTitle(), notificationRequest.getBody());
    }
}
