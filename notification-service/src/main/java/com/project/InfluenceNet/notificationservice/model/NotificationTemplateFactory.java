package com.project.InfluenceNet.notificationservice.model;

import com.project.InfluenceNet.contracts.notification.NotificationType;
import org.springframework.stereotype.Component;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class NotificationTemplateFactory {

    private Map<String, NotificationTemplate> templateMap;

    public NotificationTemplateFactory(List<NotificationTemplate> templates){
        this.templateMap = templates.stream()
                .collect(Collectors.toMap(
                        NotificationTemplate::getType,
                        c -> c
                ));
        System.out.println(templates);
    }


    public NotificationTemplate get(NotificationType notificationType) throws InvalidParameterException{
        return templateMap.get(notificationType.toString());
    }
}
