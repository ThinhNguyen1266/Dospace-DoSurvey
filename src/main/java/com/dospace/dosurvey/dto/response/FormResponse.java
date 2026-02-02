package com.dospace.dosurvey.dto.response;

import com.dospace.dosurvey.entity.enums.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FormResponse {
    String id;
    String tenantId;
    String ownerId;
    String title;
    String description;
    String publicLink;
    String editorLink;
    String qrUrl;
    String logoImage;
    String bannerImage;
    String primaryColor;
    String backgroundColor;
    String headerFont;
    String questionFont;
    String textFont;
    FormStatus status;
    DeleteStatus deleteStatus;
    FormCloseMode closeMode;
    FormFor formFor;
    Integer maxResponse;
    Boolean autoThanks;
    String thankYouMessage;
    LocalDateTime validFrom;
    LocalDateTime validThrough;
    AccessPermission editorAccess;
    AccessPermission responderAccess;
    Boolean editorsCanShare;
    String categoryId;
    String categoryName;
    String groupId;
    String groupName;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    List<FormPageResponse> pages;
}
