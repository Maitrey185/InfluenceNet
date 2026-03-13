package com.project.InfluenceNet.socialConnector.exception;

import org.springframework.http.HttpStatus;

public class SocialIngestionException extends InstagramConnectorException {

    public SocialIngestionException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "SOCIAL_INGESTION_ERROR", message);
    }

    public SocialIngestionException(String message, Throwable cause) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "SOCIAL_INGESTION_ERROR", message, cause);
    }
}
