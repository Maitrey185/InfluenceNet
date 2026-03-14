package com.project.InfluenceNet.notificationservice.model;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ChannelFactory {

    private final Map<String, NotificationChannel> channelMap;

    public ChannelFactory(List<NotificationChannel> channels) {
        this.channelMap = channels.stream()
                .collect(Collectors.toMap(
                        NotificationChannel::getType,
                        c -> c
                ));
        System.out.println(channelMap);
    }

    public NotificationChannel get(String type) {
        NotificationChannel channel = channelMap.get(type);
        if (channel == null) {
            throw new IllegalArgumentException("Unsupported channel: " + type);
        }
        return channel;
    }
}
