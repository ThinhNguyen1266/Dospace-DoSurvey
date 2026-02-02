package com.dospace.dosurvey.web;

import com.dospace.dosurvey.context.UserContextHolder;
import com.dospace.dosurvey.dto.UserContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;

/**
 * Filter to extract user information from request headers (set by API Gateway
 * after JWT validation)
 * and populate UserContextHolder for downstream services.
 */
@Component
@Order(2) // Run after TenantContextFilter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class UserContextFilter extends OncePerRequestFilter {

    private static final String HEADER_ACCOUNT_ID = "X-Account-Id";
    private static final String HEADER_EMAIL = "X-Email";
    private static final String HEADER_USER_TYPE = "X-User-Type";
    private static final String HEADER_JWT_ID = "X-JWT-ID";
    private static final String HEADER_TENANT_ID = "X-Tenant-Id";
    private static final String HEADER_TENANT_TYPE = "X-Tenant-Type";
    private static final String HEADER_ISSUED_AT = "X-Issued-At";
    private static final String HEADER_EXPIRES_AT = "X-Expires-At";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String accountId = request.getHeader(HEADER_ACCOUNT_ID);

        if (StringUtils.hasText(accountId)) {
            UserContext userContext = UserContext.builder()
                    .userId(accountId)
                    .email(request.getHeader(HEADER_EMAIL))
                    .userType(request.getHeader(HEADER_USER_TYPE))
                    .jwtId(request.getHeader(HEADER_JWT_ID))
                    .tenantId(request.getHeader(HEADER_TENANT_ID))
                    .tenantType(request.getHeader(HEADER_TENANT_TYPE))
                    .issuedAt(parseEpochMillis(request.getHeader(HEADER_ISSUED_AT)))
                    .expiresAt(parseEpochMillis(request.getHeader(HEADER_EXPIRES_AT)))
                    .build();

            UserContextHolder.setCurrentUser(userContext);
            log.debug("Set user context: accountId={}, tenantId={}, tenantType={}",
                    accountId, userContext.getTenantId(), userContext.getTenantType());
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            UserContextHolder.clear();
        }
    }

    /**
     * Parse date from epoch milliseconds string.
     * API Gateway sends dates as epoch millis for consistent parsing.
     */
    private Date parseEpochMillis(String epochMillisStr) {
        if (!StringUtils.hasText(epochMillisStr)) {
            return null;
        }
        try {
            return new Date(Long.parseLong(epochMillisStr));
        } catch (NumberFormatException e) {
            log.warn("Failed to parse epoch millis: {}", epochMillisStr);
            return null;
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/actuator")
                || path.startsWith("/api/internal")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-ui");
    }
}
