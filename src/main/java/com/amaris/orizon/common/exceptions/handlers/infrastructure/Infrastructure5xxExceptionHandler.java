package com.amaris.orizon.common.exceptions.handlers.infrastructure;

import com.amaris.orizon.common.exceptions.ErrorResponse;
import com.amaris.orizon.common.exceptions.infrastructure.Infrastructure5xxException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;

import static com.amaris.orizon.common.utilis.InternalErrors.INFRASTRUCTURE5xx_ERROR;


@RestControllerAdvice
@Slf4j
public class Infrastructure5xxExceptionHandler {

    @ExceptionHandler(Infrastructure5xxException.class)
    public ResponseEntity<ErrorResponse> handle(
            Infrastructure5xxException exception) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .path(exception.getUriPath())
                .statusCode(500)
                .errorCode(INFRASTRUCTURE5xx_ERROR.getInternalCode())
                .timestamp(OffsetDateTime.now())
                .message(exception.getMessage())
                .subErrors(exception.getSubErrors())
                .build();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }
}
