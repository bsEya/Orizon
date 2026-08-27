package com.amaris.orizon.common.logging;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.amaris.orizon.common.utilis.LogsConstant.CORRELATION_ID_HEADER_NAME;
import static com.amaris.orizon.common.utilis.LogsConstant.CORRELATION_ID_LOG_VAR_NAME;
import static com.amaris.orizon.common.utilis.LogsConstant.ORIGIN_ID_HEADER_NAME;
import static com.amaris.orizon.common.utilis.LogsConstant.ORIGIN_ID_LOG_VAR_NAME;
import static com.amaris.orizon.common.utilis.LogsConstant.SESSION_ID_HEADER_NAME;
import static com.amaris.orizon.common.utilis.LogsConstant.SESSION_ID_LOG_VAR_NAME;

@Slf4j
@Component
public class HttpLogInterceptor extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        long startTime = System.nanoTime();

        String correlationId = LoggingMethods.extractHeaderValue(
                CORRELATION_ID_HEADER_NAME,
                request,
                UUID.randomUUID().toString());

        String sessionId = request.getHeader(SESSION_ID_HEADER_NAME);
        String originId = request.getHeader(ORIGIN_ID_HEADER_NAME);

        String httpMethod = request.getMethod();
        String endPoint = request.getRequestURI();

        Map<String, String> logData = new HashMap<>();
        logData.put(CORRELATION_ID_LOG_VAR_NAME, correlationId);
        logData.put(SESSION_ID_LOG_VAR_NAME, sessionId);
        logData.put(ORIGIN_ID_LOG_VAR_NAME, originId);
        logData.put("httpMethod", httpMethod);
        logData.put("endPoint", endPoint);

        LoggingMethods.insertIntoMDC(logData);

        log.debug("Request received - hasBody={}", request.getContentLengthLong() > 0);

        log.info(
                "REQUEST RECEIVED method={} correlationId={} sessionId={} originId={}",
                httpMethod,
                correlationId,
                sessionId,
                originId
        );

        try {
            filterChain.doFilter(request, response);
        } finally {
            long processingTime =
                    TimeUnit.NANOSECONDS.toMicros(System.nanoTime() - startTime);

            log.info(
                    "RESPONSE SENT status={} processingTimeMicros={}",
                    response.getStatus(),
                    processingTime
            );

            MDC.clear();
        }
    }
}