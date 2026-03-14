package com.project.InfluenceNet.notificationservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "notification_preferences")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationPreference {

    @EmbeddedId
    private NotificationPreferenceId id;

    @Column(name = "enabled")
    private boolean enabled = true;

    public UUID getUserId() {
        return id.getUserId();
    }

    public String getEventType() {
        return id.getEventType();
    }

    public String getChannel() {
        return id.getChannel();
    }
}
