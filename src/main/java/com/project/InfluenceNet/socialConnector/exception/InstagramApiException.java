package com.project.InfluenceNet.socialConnector.exception;

import org.springframework.http.HttpStatus;

public class InstagramApiException extends InstagramConnectorException {

    private final String responseBody;

    public InstagramApiException(HttpStatus status, String errorCode, String message, String responseBody) {
        super(status, errorCode, message);
        this.responseBody = responseBody;
    }

    public InstagramApiException(HttpStatus status, String errorCode, String message, String responseBody, Throwable cause) {
        super(status, errorCode, message, cause);
        this.responseBody = responseBody;
    }

    public String getResponseBody() {
        return responseBody;
    }
}
