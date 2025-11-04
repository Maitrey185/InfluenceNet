package com.project.InfluenceNet.socialConnector.controller;

import com.project.InfluenceNet.socialConnector.client.InstagramClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/instagram")
public class InstragramController {

    @Autowired
    private InstagramClient instagramClient;

    @GetMapping("/data")
    public Map<String, Object> getInstagramUserData() {
        return instagramClient.getInstagramUserData();
    }
}
