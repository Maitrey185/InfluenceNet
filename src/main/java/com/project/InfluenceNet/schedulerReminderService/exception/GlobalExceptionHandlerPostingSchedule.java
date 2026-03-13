package com.project.InfluenceNet.schedulerReminderService.exception;

import com.project.InfluenceNet.schedulerReminderService.controller.PostingSchedulesController;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice(basePackages = "com.project.InfluenceNet.schedulerReminderService")
public class GlobalExceptionHandlerPostingSchedule {


        @ExceptionHandler(PostingScheduleNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleNotFound(
                PostingScheduleNotFoundException ex) {
            return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
        }

        @ExceptionHandler(DuplicatePostingScheduleException.class)
        public ResponseEntity<ErrorResponse> handleDuplicate(
                DuplicatePostingScheduleException ex) {

            return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleGeneric(
                Exception ex) {

            return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Internal server error");
        }

        // HELPER METHOD
        private ResponseEntity<ErrorResponse> buildResponse(
                HttpStatus status,
                String message) {

            ErrorResponse error = new ErrorResponse(
                    status.value(),
                    message,
                    LocalDateTime.now()
            );

            return ResponseEntity.status(status).body(error);
        }

        public record ErrorResponse(
                int status,
                String message,
                LocalDateTime timestamp
        ) {}


}
