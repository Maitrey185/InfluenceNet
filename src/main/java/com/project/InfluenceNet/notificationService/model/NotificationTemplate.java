package com.project.InfluenceNet.notificationService.model;

import com.project.InfluenceNet.notificationService.event.NotificationEvent;

public abstract class NotificationTemplate {

    public abstract NotificationRequest render(NotificationEvent notificationEvent);
}
