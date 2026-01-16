package com.dospace.dosurvey.dto.internal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenantMailConfigResponse {
  private String host;
  private Integer port;
  private String from;
  private String username;
  private String password;
  private String agentAlias;
  private String provider;
}
