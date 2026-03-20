package com.project.InfluenceNet.collaborationNeo4jService.service;

import com.project.InfluenceNet.collaborationNeo4jService.dto.InfluencerNodeProjection;
import com.project.InfluenceNet.collaborationNeo4jService.nodes.InfluencerNode;
import com.project.InfluenceNet.collaborationNeo4jService.repository.InfluencerNodeRepository;
import com.project.InfluenceNet.contracts.notification.CollabPayload;
import com.project.InfluenceNet.contracts.notification.NotificationEvent;
import com.project.InfluenceNet.contracts.notification.Payload;
import com.project.InfluenceNet.contracts.notification.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CollabCalculateScheduler {

    private final InfluencerNodeRepository influencerNodeRepository;
    private final CollaborationService collaborationService;
    private final CollabNotificationPublisher collabNotificationPublisher;

    @Scheduled(cron = "0 0 2 * * *")   // runs daily at 2 AM
    public void generateDailyRecommendations() {

        List<InfluencerNodeProjection> influencers = influencerNodeRepository.findAllProjectedBy();
        for (InfluencerNodeProjection influencer : influencers) {
            List<InfluencerNode> recs =
                    collaborationService.recommendedCollaborators(influencer.getId());

            List<String> ls = recs.stream().map(rec-> rec.getName()).toList();

            Payload payload = CollabPayload.builder()
                    .collaboratorName(ls)
                    .email(influencer.getEmail())
                    .build();


            NotificationEvent notificationEvent = NotificationEvent.builder()
                    .userId(influencer.getId())
                    .notificationType(NotificationType.COLLAB)
                    .timestamp(Instant.now())
                    .payload(payload)
                    .build();

            collabNotificationPublisher.publishNotificationEvent(notificationEvent);
        }
    }

}
