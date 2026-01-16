package com.dospace.dosurvey.dto.response;

import com.dospace.dosurvey.entity.enums.AccessPermission;
import com.dospace.dosurvey.entity.enums.FormRole;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SharingSettingsResponse {
    String ownerId;
    String ownerEmail;
    List<CollaboratorResponse> collaborators;
    AccessSettings editorAccess;
    AccessSettings responderAccess;
    boolean editorsCanShare;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class CollaboratorResponse {
        String accountId;
        String username;
        String avatarUrl;
        String email;
        FormRole role;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class AccessSettings {
        AccessPermission currentLevel;
        List<AccessPermissionOption> availableOptions;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class AccessPermissionOption {
        AccessPermission value;
        String label;
        String description;
    }
}
