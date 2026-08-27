package com.amaris.orizon.common.exceptions.handlers.infrastructure;

import com.amaris.orizon.common.exceptions.ErrorResponse;
import com.amaris.orizon.common.exceptions.infrastructure.InfrastructureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;

import static com.amaris.orizon.common.utilis.InternalErrors.INFRASTRUCTURE_ERROR;


@RestControllerAdvice
public class InfrastructureExceptionHandler {

    @ExceptionHandler(InfrastructureException.class)
    public ResponseEntity<ErrorResponse> handle(
            InfrastructureException exception) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .path(exception.getUriPath())
                .statusCode(500)
                .errorCode(INFRASTRUCTURE_ERROR.getInternalCode())
                .timestamp(OffsetDateTime.now())
                .message(exception.getMessage())
                .subErrors(null)
                .build();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }
}
