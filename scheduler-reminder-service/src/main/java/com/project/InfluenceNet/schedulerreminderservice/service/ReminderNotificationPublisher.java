package com.project.InfluenceNet.schedulerreminderservice.service;

import com.project.InfluenceNet.contracts.notification.NotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReminderNotificationPublisher {

    public static final String TOPIC_SEND_NOTIFICATION = "send.notification";

    private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;

    public void publishNotificationEvent(NotificationEvent event) {
        kafkaTemplate.send(TOPIC_SEND_NOTIFICATION, event);
    }
}
