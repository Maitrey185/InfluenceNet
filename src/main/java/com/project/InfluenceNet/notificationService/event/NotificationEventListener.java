package com.project.InfluenceNet.notificationService.event;

import com.project.InfluenceNet.analyticsService.service.AnalyticsService;
import com.project.InfluenceNet.analyticsService.service.EngagementHeatmapService;
import com.project.InfluenceNet.notificationService.model.NotificationRequest;
import com.project.InfluenceNet.notificationService.model.NotificationTemplate;
import com.project.InfluenceNet.notificationService.model.NotificationTemplateFactory;
import com.project.InfluenceNet.socialConnector.documents.RawInsights;
import com.project.InfluenceNet.socialConnector.events.InsightsfetchedEvent;
import com.project.InfluenceNet.socialConnector.repository.RawInsightsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationEventListener {

    private final NotificationTemplateFactory notificationTemplateFactory;

    public static final String TOPIC_SEND_NOTIFICATION = "send.notification";

    //    @KafkaListener(topics = TOPIC_SEND_NOTIFICATION)
    public void handleSendNotification(NotificationEvent event){
        log.info("Received send notification event: {}", event);

        NotificationTemplate notificationTemplate = notificationTemplateFactory.get(event.getNotificationType());

        NotificationRequest notificationRequest = notificationTemplate.build(event);

    }
}
