package com.project.InfluenceNet.notificationService.model;

import java.security.InvalidParameterException;

public class NotificationTemplateFactory {

    public NotificationTemplate get(NotificationType notificationType) throws InvalidParameterException{
        if(notificationType==NotificationType.COLLAB){
            return new CollabNotificationTemplate();
        }
        else if(notificationType==NotificationType.POST_REMINDER){
            return new PostReminderNotificationTemplate();
        }
        else throw new InvalidParameterException();
    }
}
