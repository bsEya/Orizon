package com.amaris.orizon.common.exceptions;

import lombok.Builder;
@Builder
public record SubError(
        String message,
        String cause,
        int errorCode

) {
}

