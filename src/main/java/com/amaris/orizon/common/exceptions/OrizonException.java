package com.amaris.orizon.common.exceptions;

import lombok.Getter;
@Getter
public class OrizonException extends RuntimeException {
    private final String uriPath;
    public OrizonException(String message, String uriPath) {
        super(message);
        this.uriPath = uriPath;
    }
}
