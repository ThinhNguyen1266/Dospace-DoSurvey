package com.dospace.dosurvey.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserContext {
  String userId;
  String email;
  String userType;
  String jwtId;
  String tenantId;
  Date issuedAt;
  Date expiresAt;
}
