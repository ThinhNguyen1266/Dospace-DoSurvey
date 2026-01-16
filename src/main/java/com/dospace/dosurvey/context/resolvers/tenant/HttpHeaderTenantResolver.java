package com.dospace.dosurvey.context.resolvers.tenant;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Slf4j
public class HttpHeaderTenantResolver implements TenantResolver<HttpServletRequest> {

  private static final String TENANT_HEADER = "X-Tenant-Id";

  @Override
  public String resolveTenantIdentifier(HttpServletRequest request) {
    String tenantId = request.getHeader(TENANT_HEADER);
    log.debug("Resolving tenant identifier from header {}: {}", TENANT_HEADER, tenantId);
    if (StringUtils.hasText(tenantId)) {
      return tenantId;
    }
    return null;
  }
}

