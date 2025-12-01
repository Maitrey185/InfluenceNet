package com.project.InfluenceNet.enrichmentService.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class PostEnrichmentHelper {

    private static final Pattern HASHTAG_PATTERN = Pattern.compile("#(\\w+)");
    private static final Pattern MENTION_PATTERN = Pattern.compile("@(\\w+)");

    public List<String> extractHashtags(String text) {
        if (text == null) return List.of();
        Matcher m = HASHTAG_PATTERN.matcher(text);
        List<String> tags = new ArrayList<>();
        while (m.find()) {
            tags.add(m.group(1).toLowerCase());
        }
        return tags;
    }

    public List<String> extractMentions(String text) {
        if (text == null) return List.of();
        Matcher m = MENTION_PATTERN.matcher(text);
        List<String> tags = new ArrayList<>();
        while (m.find()) {
            tags.add("@" + m.group(1));
        }
        return tags;
    }



}
