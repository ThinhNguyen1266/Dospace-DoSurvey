package com.dospace.dosurvey.dto.response;

import com.dospace.dosurvey.entity.enums.AccessPermission;
import com.dospace.dosurvey.entity.enums.DeleteStatus;
import com.dospace.dosurvey.entity.enums.FormCloseMode;
import com.dospace.dosurvey.entity.enums.FormFor;
import com.dospace.dosurvey.entity.enums.FormStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    LocalDateTime validFrom;
    LocalDateTime validThrough;
    AccessPermission editorAccess;
    AccessPermission responderAccess;
    Boolean editorsCanShare;
    String categoryId;
    String categoryName;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    List<FormPageResponse> pages;
}
