package com.amaris.orizon.common.exceptions.handlers.domain;


import com.amaris.orizon.common.exceptions.ErrorResponse;
import com.amaris.orizon.common.exceptions.domain.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;

import static com.amaris.orizon.common.utilis.InternalErrors.RESOURCE_NOT_FOUND;


@RestControllerAdvice
public class ResourceNotFoundExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException exception) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .path(exception.getUriPath())
                .statusCode(HttpStatus.NOT_FOUND.value())
                .errorCode(RESOURCE_NOT_FOUND.getInternalCode())
                .timestamp(OffsetDateTime.now())
                .message(RESOURCE_NOT_FOUND.getErrorMessage())
                .subErrors(exception.getSubErrors())
                .build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }
}