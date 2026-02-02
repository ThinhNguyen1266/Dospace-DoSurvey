package com.dospace.dosurvey.utils;

import com.dospace.dosurvey.context.UserContextHolder;
import com.dospace.dosurvey.dto.UserContext;

/**
 * Utility class for extracting user information from the current request
 * context.
 */
public final class SecurityContextUtil {

    private SecurityContextUtil() {
        // Utility class, no instantiation
    }

    /**
     * Get the current authenticated user's account ID.
     */
    public static String getCurrentAccountId() {
        return UserContextHolder.getRequiredAccountId();
    }

    /**
     * Get the current authenticated user's email.
     */
    public static String getCurrentEmail() {
        UserContext user = UserContextHolder.getRequiredUser();
        String email = user.getEmail();
        if (email == null || email.isBlank()) {
            throw new IllegalStateException("Email not found in user context");
        }
        return email;
    }

    /**
     * Check if a user is currently authenticated.
     */
    public static boolean isAuthenticated() {
        return UserContextHolder.getCurrentUser() != null;
    }
}
