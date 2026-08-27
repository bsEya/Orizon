package com.amaris.orizon.exception.handler;


import com.amaris.orizon.common.exceptions.ErrorResponse;
import com.amaris.orizon.common.exceptions.OrizonException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;

import static com.amaris.orizon.common.utilis.InternalErrors.BACK_END_EXCEPTION;

@RestControllerAdvice
public class OrizonExceptionHandler {

    @ExceptionHandler(OrizonException.class)
    public ResponseEntity<ErrorResponse> handleOrizonException(OrizonException exception) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .path(exception.getUriPath())
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .errorCode(BACK_END_EXCEPTION.getInternalCode())
                .timestamp(OffsetDateTime.now())
                .message(exception.getMessage())
                .subErrors(null)
                .build();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }
}