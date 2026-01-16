package com.dospace.dosurvey.context;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

@Slf4j
public class TenantContextHolder {
  private static final ThreadLocal<String> tenantIdentifier = new ThreadLocal<>();

  public static void setCurrentTenant(String tenant) {
    Assert.hasText(tenant, "tenant cannot be empty");
    log.trace("Setting current tenant to: {}", tenant);
    tenantIdentifier.set(tenant);
  }

  public static String getCurrentTenant() {
    return tenantIdentifier.get();
  }

  public static String getRequiredTenantIdentifier() {
    var tenant = getCurrentTenant();
    if (!StringUtils.hasText(tenant)) {
      throw new RuntimeException("No tenant found in the current context");
    }
    return tenant;
  }

  public static void clear() {
    log.trace("Clearing current tenant");
    tenantIdentifier.remove();
  }
}

