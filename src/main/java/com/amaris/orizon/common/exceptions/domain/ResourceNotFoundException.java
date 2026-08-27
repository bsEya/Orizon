package com.amaris.orizon.common.exceptions.domain;


import com.amaris.orizon.common.exceptions.SubError;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;
@Getter
public class ResourceNotFoundException extends DomainException {
    private final Set<SubError> subErrors;

    @Builder(builderMethodName = "resourceNotFoundExceptionBuilder")
    public ResourceNotFoundException(String message, String uriPath, Set<SubError> subErrors) {
        super(message, uriPath);
        this.subErrors = subErrors;

    }
}
