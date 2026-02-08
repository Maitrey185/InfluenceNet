package com.project.InfluenceNet.notificationService.event;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("Notif-Event")
@RequiredArgsConstructor
public class NotificationEventDummyController {

    private final NotificationEventListener notificationEventListener;

    @PostMapping("/get")
    public void call(@RequestBody NotificationEvent notificationEvent){
        notificationEventListener.handleSendNotification(notificationEvent);
    }
}
