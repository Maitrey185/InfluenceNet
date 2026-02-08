package com.project.InfluenceNet.notificationService;

import com.project.InfluenceNet.notificationService.model.NotificationPreference;
import com.project.InfluenceNet.notificationService.model.NotificationPreferenceId;
import com.project.InfluenceNet.notificationService.model.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationPreferenceRepository extends JpaRepository<NotificationPreference, NotificationPreferenceId> {

    List<NotificationPreference> findByIdUserIdAndIdEventType(UUID userId, String notificationType);

}
