package com.project.InfluenceNet.notificationservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationPreferenceId implements Serializable {

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "event_type")
    private String eventType; // e.g. POST_REMINDER, COLLAB_SUGGESTION

    @Column(name = "channel")
    private String channel;   // EMAIL, PUSH
}
