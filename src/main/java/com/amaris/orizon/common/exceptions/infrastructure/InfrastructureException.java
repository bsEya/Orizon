package com.amaris.orizon.common.exceptions.infrastructure;


import com.amaris.orizon.common.exceptions.OrizonException;
import lombok.Builder;

public class InfrastructureException extends OrizonException {

    @Builder(builderMethodName = "infrastructureExceptionBuilder")
    public InfrastructureException(String message, String uriPath) {
        super(message, uriPath);
    }

    public InfrastructureException() {
        super(null, null);
    }
}