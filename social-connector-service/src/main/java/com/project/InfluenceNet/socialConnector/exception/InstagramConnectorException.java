package com.project.InfluenceNet.socialConnector.exception;

import org.springframework.http.HttpStatus;

public class InstagramConnectorException extends RuntimeException {

    private final HttpStatus status;
    private final String errorCode;

    public InstagramConnectorException(HttpStatus status, String errorCode, String message) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }

    public InstagramConnectorException(HttpStatus status, String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
        this.errorCode = errorCode;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
