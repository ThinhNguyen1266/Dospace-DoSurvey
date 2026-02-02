package com.dospace.dosurvey.web;

import com.dospace.dosurvey.context.TenantContextHolder;
import com.dospace.dosurvey.context.resolvers.tenant.TenantResolver;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class TenantContextFilter extends OncePerRequestFilter {

    TenantResolver<HttpServletRequest> tenantResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String identifier = tenantResolver.resolveTenantIdentifier(request);
        if (StringUtils.hasText(identifier)) {
            TenantContextHolder.setCurrentTenant(identifier);
            log.debug("Set tenant context: {}", identifier);
        }
        // Let request continue even without tenant - validation is done at API Gateway
        try {
            filterChain.doFilter(request, response);
        } finally {
            TenantContextHolder.clear();
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/actuator")
                || path.startsWith("/api/internal")
                || path.startsWith("/api/forms/public")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-ui");
    }
}
