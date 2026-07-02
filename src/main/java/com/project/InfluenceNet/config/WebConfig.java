package com.project.InfluenceNet.config;

import com.project.InfluenceNet.contracts.InfluencerPostContract.Platform;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToPlatformConverter());
    }

    private static class StringToPlatformConverter implements Converter<String, Platform> {
        @Override
        public Platform convert(String source) {
            try {
                return Platform.valueOf(source.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid platform: " + source + ". Valid values: " + 
                    java.util.Arrays.toString(Platform.values()));
            }
        }
    }
}
