package com.amaris.orizon.common.exceptions.handlers.infrastructure;



import com.amaris.orizon.common.exceptions.ErrorResponse;
import com.amaris.orizon.common.exceptions.infrastructure.Infrastructure4xxException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;

import static com.amaris.orizon.common.utilis.InternalErrors.INFRASTRUCTURE4xx_ERROR;


@RestControllerAdvice
public class Infrastructure4xxExceptionHandler {

    @ExceptionHandler(Infrastructure4xxException.class)
    public ResponseEntity<ErrorResponse> handle(
            Infrastructure4xxException exception) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .path(exception.getUriPath())
                .statusCode(500)
                .errorCode(INFRASTRUCTURE4xx_ERROR.getInternalCode())
                .timestamp(OffsetDateTime.now())
                .message(INFRASTRUCTURE4xx_ERROR.getErrorMessage())
                .subErrors(exception.getSubErrors())
                .build();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }
}