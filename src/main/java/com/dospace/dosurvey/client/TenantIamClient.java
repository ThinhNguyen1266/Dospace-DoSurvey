package com.dospace.dosurvey.client;

import com.dospace.dosurvey.dto.internal.TenantMailConfigResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "tenant-iam-service", url = "${tenant-iam-service.url:http://localhost:8081}")
public interface TenantIamClient {

  @GetMapping("/api/internal/tenants/{tenantId}/mail-config")
  TenantMailConfigResponse getTenantMailConfig(@PathVariable("tenantId") String tenantId);
}
