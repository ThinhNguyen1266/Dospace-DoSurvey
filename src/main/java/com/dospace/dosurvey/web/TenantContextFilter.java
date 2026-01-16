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
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(1) // Run before UserContextFilter
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
    } else {
      // If not found, we might want to throw exception or just proceed without
      // context
      // Depending on requirements. Previous implementation sent 400.
      // Vivista-api throws exception.
      // Let's send 400 to match previous behavior for now, or throw exception if
      // strict.
      // The plan said "Matches vivista-api implementation".
      // Vivista throws TenantResolutionException.
      // But here let's stick to sending error response for now to avoid creating new
      // exception classes if not needed,
      // OR we can just return 400 error.
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing X-Tenant-Id header");
      return;
    }
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

