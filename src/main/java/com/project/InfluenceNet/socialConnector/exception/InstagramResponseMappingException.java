package com.project.InfluenceNet.socialConnector.exception;

import org.springframework.http.HttpStatus;

public class InstagramResponseMappingException extends InstagramConnectorException {

    public InstagramResponseMappingException(String message) {
        super(HttpStatus.BAD_GATEWAY, "INSTAGRAM_RESPONSE_MAPPING_ERROR", message);
    }

    public InstagramResponseMappingException(String message, Throwable cause) {
        super(HttpStatus.BAD_GATEWAY, "INSTAGRAM_RESPONSE_MAPPING_ERROR", message, cause);
    }
}
