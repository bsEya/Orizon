package com.amaris.orizon.common.logging;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import java.util.Map;

public final class LoggingMethods {

    private LoggingMethods() {
    }

    public static String extractHeaderValue(
            String headerKey,
            HttpServletRequest request,
            String defaultValue) {

        String headerValue = request.getHeader(headerKey);
        return StringUtils.isNotBlank(headerValue)
                ? headerValue
                : defaultValue;
    }

    public static void insertIntoMDC(Map<String, String> data) {
        if (data != null) {
            data.forEach((key, value) -> {
                if (value != null) {
                    MDC.put(key, value);
                }
            });
        }
    }

    public static void clearMDC() {
        MDC.clear();
    }
}