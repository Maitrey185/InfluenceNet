package com.project.InfluenceNet.socialConnector.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class InstagramClient {

    @Autowired
    private WebClient webClient;

    public Map<String, Object> getInstagramData() {
        String fields = String.join(",",
                "biography",
                "followers_count",
                "follows_count",
                "id",
                "media_count",
                "name",
                "profile_picture_url",
                "username",
                "website"
        );

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("graph.instagram.com")
                        .path("/v24.0/{userId}")
                        .queryParam("fields", fields)
                        .queryParam("access_token", "dummy")
                        .build("dummy")
                )
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();

    }



}
