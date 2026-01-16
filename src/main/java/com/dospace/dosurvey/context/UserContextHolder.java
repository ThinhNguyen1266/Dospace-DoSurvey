package com.dospace.dosurvey.context;

import com.dospace.dosurvey.dto.UserContext;
import lombok.extern.slf4j.Slf4j;

/**
 * Holds user context for the current thread.
 * Stores full UserContext object extracted from JWT claims.
 */
@Slf4j
public class UserContextHolder {
  private static final ThreadLocal<UserContext> userContext = new ThreadLocal<>();

  public static void setCurrentUser(UserContext context) {
    if (context == null) {
      throw new IllegalArgumentException("UserContext cannot be null");
    }
    log.trace("Setting current user: {}", context.getUserId());
    userContext.set(context);
  }

  public static UserContext getCurrentUser() {
    return userContext.get();
  }

  public static UserContext getRequiredUser() {
    UserContext context = getCurrentUser();
    if (context == null) {
      throw new RuntimeException("No user context found in the current request");
    }
    return context;
  }

  public static String getCurrentAccountId() {
    UserContext context = getCurrentUser();
    return context != null ? context.getUserId() : null;
  }

  public static String getRequiredAccountId() {
    return getRequiredUser().getUserId();
  }

  public static void clear() {
    log.trace("Clearing current user context");
    userContext.remove();
  }
}

