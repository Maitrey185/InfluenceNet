package com.project.InfluenceNet.notificationservice.model;

import com.project.InfluenceNet.contracts.notification.NotificationEvent;
import org.springframework.stereotype.Component;

@Component
public abstract class NotificationTemplate {

    public abstract String getType();

    public final NotificationRequest build(NotificationEvent e) {
        validate(e);
        return render(e);
    }

    protected void validate(NotificationEvent e) {
        if (e == null) throw new IllegalArgumentException("Event cannot be null");
        if (e.getUserId() == null) throw new IllegalArgumentException("User missing");
    }

    public abstract NotificationRequest render(NotificationEvent notificationEvent);
}
