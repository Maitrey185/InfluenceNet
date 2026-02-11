package com.project.InfluenceNet.collaborationNeo4jService.service;

import com.project.InfluenceNet.notificationService.event.NotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CollabNotificationPublisher {

    public static final String TOPIC_SEND_NOTIFICATION = "send.notification";

    private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;

    public void publishNotificationEvent(NotificationEvent event) {
        kafkaTemplate.send(TOPIC_SEND_NOTIFICATION, event);
    }
}
