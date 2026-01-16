package com.dospace.dosurvey.config;

import com.dospace.dosurvey.context.UserContextHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

/**
 * Configuration for JPA Auditing.
 * Provides:
 * - AuditorAware: current user ID from UserContextHolder
 * - DateTimeProvider: LocalDateTime in UTC+7 timezone
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider", dateTimeProviderRef = "auditingDateTimeProvider")
public class AuditingConfig {

  private static final ZoneId VIETNAM_ZONE = ZoneId.of("Asia/Ho_Chi_Minh");

  @Bean
  public AuditorAware<String> auditorProvider() {
    return () -> Optional.ofNullable(UserContextHolder.getCurrentAccountId());
  }

  @Bean
  public DateTimeProvider auditingDateTimeProvider() {
    return () -> Optional.of(LocalDateTime.now(VIETNAM_ZONE));
  }
}
