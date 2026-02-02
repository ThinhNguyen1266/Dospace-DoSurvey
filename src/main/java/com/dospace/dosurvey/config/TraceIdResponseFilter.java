package com.dospace.dosurvey.config;

import io.micrometer.tracing.Tracer;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Filter to add trace ID to response headers for easy correlation with Zipkin
 */
@Component
@RequiredArgsConstructor
public class TraceIdResponseFilter implements Filter {

    private final Tracer tracer;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (response instanceof HttpServletResponse httpResponse) {
            var span = tracer.currentSpan();
            if (span != null) {
                var traceId = span.context().traceId();
                httpResponse.setHeader("X-Trace-Id", traceId);
            }
        }

        chain.doFilter(request, response);
    }
}

