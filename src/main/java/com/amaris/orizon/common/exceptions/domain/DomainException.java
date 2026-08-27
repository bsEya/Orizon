package com.amaris.orizon.common.exceptions.domain;

import com.amaris.orizon.common.exceptions.OrizonException;
import lombok.Getter;

@Getter
public class DomainException extends OrizonException {

    public DomainException(String message, String uriPath) {
        super(message, uriPath);
    }
}
