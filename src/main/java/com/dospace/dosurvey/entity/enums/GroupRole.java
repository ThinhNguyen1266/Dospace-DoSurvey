package com.dospace.dosurvey.entity.enums;

public enum GroupRole {
    OWNER,   // Full control, can delete group
    ADMIN,   // Can manage members and forms
    MEMBER,  // Can create and edit forms
    VIEWER   // Read-only access
}
