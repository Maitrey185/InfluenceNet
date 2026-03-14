package com.project.InfluenceNet.notificationservice;

import com.project.InfluenceNet.notificationservice.model.NotificationPreference;
import com.project.InfluenceNet.notificationservice.model.NotificationPreferenceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationPreferenceRepository extends JpaRepository<NotificationPreference, NotificationPreferenceId> {

    List<NotificationPreference> findByIdUserIdAndIdEventType(UUID userId, String notificationType);

}
