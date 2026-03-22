package com.project.InfluenceNet.socialConnector.exception;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandlerSocialConnector {

    @ExceptionHandler(InstagramConnectorException.class)
    public ResponseEntity<ApiError> handleInstagramConnectorException(InstagramConnectorException ex) {
        ApiError error = new ApiError(
                ex.getStatus().value(),
                ex.getErrorCode(),
                ex.getMessage(),
                LocalDateTime.now(),
                null
        );
        return ResponseEntity.status(ex.getStatus()).body(error);
    }

    @ExceptionHandler(InstagramApiException.class)
    public ResponseEntity<ApiError> handleInstagramApiException(InstagramApiException ex) {
        ApiError error = new ApiError(
                ex.getStatus().value(),
                ex.getErrorCode(),
                ex.getMessage(),
                LocalDateTime.now(),
                ex.getResponseBody()
        );
        return ResponseEntity.status(ex.getStatus()).body(error);
    }

    @ExceptionHandler({InstagramResponseMappingException.class, SocialIngestionException.class})
    public ResponseEntity<ApiError> handleInstagramMappingOrIngestion(RuntimeException ex) {
        if (ex instanceof InstagramConnectorException ice) {
            ApiError error = new ApiError(
                    ice.getStatus().value(),
                    ice.getErrorCode(),
                    ice.getMessage(),
                    LocalDateTime.now(),
                    null
            );
            return ResponseEntity.status(ice.getStatus()).body(error);
        }

        ApiError error = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "INTERNAL_ERROR",
                ex.getMessage(),
                LocalDateTime.now(),
                null
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiError> handleDataAccessException(DataAccessException ex) {
        ApiError error = new ApiError(
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                "DATABASE_ERROR",
                "Database operation failed",
                LocalDateTime.now(),
                ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgumentException(IllegalArgumentException ex) {
        ApiError error = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "BAD_REQUEST",
                ex.getMessage(),
                LocalDateTime.now(),
                null
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGlobalException(Exception ex) {
        ApiError error = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "BAD_REQUEST",
                "An unexpected error occurred: " + ex.getMessage(),
                LocalDateTime.now(),
                null
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    // Error response classes
    public record ApiError(int status, String errorCode, String message, LocalDateTime timestamp, String details) {}

}
