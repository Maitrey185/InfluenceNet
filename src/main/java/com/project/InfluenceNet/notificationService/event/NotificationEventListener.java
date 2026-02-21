package com.project.InfluenceNet.notificationService.event;

import com.project.InfluenceNet.analyticsService.service.AnalyticsService;
import com.project.InfluenceNet.analyticsService.service.EngagementHeatmapService;
import com.project.InfluenceNet.notificationService.NotificationPreferenceRepository;
import com.project.InfluenceNet.notificationService.model.*;
import com.project.InfluenceNet.socialConnector.documents.RawInsights;
import com.project.InfluenceNet.socialConnector.events.InsightsfetchedEvent;
import com.project.InfluenceNet.socialConnector.repository.RawInsightsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationEventListener {

    private final NotificationTemplateFactory notificationTemplateFactory;
    private final NotificationPreferenceRepository notificationPreferenceRepository;
    private final ChannelFactory channelFactory;
    public static final String TOPIC_SEND_NOTIFICATION = "send.notification";

    @RetryableTopic(
            attempts = "2",
            backoff = @Backoff(delay = 2000),
            autoCreateTopics = "true",
            exclude = {
        MailAuthenticationException.class}

    )
    @KafkaListener(topics = TOPIC_SEND_NOTIFICATION)
    public void handleSendNotification(NotificationEvent event){
        log.info("Received send notification event: {}", event);

        NotificationTemplate notificationTemplate = notificationTemplateFactory.get(event.getNotificationType());

        NotificationRequest notificationRequest = notificationTemplate.build(event);

        for(NotificationPreference preference: notificationPreferenceRepository.findByIdUserIdAndIdEventType(event.getUserId(), event.getNotificationType().toString())){
                channelFactory.get(preference.getChannel()).send(notificationRequest);
        }

        log.info("Notification sent successfully");

    }

    @DltHandler
    public void handleDlt(NotificationEvent event) {
        log.info("Final failure. Sending to DLT: {}", event);
    }
}
