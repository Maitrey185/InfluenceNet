package com.project.InfluenceNet.enrichmentService.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.tika.langdetect.optimaize.OptimaizeLangDetector;
import org.apache.tika.language.detect.LanguageDetector;
import org.apache.tika.language.detect.LanguageResult;

import java.io.IOException;
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

    private final LanguageDetector languageDetector;

    public PostEnrichmentHelper() {
        // Initialize the language detector with Optimaize
        this.languageDetector = new OptimaizeLangDetector();
        // Load the language profiles
        ((OptimaizeLangDetector)this.languageDetector).loadModels();
    }

    public String detectLanguage(String text) {
        if (text == null || text.trim().isEmpty()) {
            return "unknown";
        }
        try {
            LanguageResult result = languageDetector.detect(text);
            return result.isReasonablyCertain() ? result.getLanguage() : "unknown";
        } catch (Exception e) {
            return "unknown";
        }
    }

    public String detectSentiment(String text) {
        if (text == null || text.trim().isEmpty()) {
            return "unknown";
        }
        try {
            LanguageResult result = languageDetector.detect(text);
            return result.isReasonablyCertain() ? result.getLanguage() : "unknown";
        } catch (Exception e) {
            return "unknown";
        }
    }






}
