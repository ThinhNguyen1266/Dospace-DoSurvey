package com.dospace.dosurvey.dto.response;

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
public class FormEditorResponse {
    String id;
    String title;
    String description;
    String publicLink;
    String editorLink;
    String status;
    String closeMode;
    String formFor;
    Integer maxResponse;
    Boolean autoThanks;
    String validFrom;
    String validThrough;
    String logoImage;
    String bannerImage;
    String primaryColor;
    String backgroundColor;
    String headerFont;
    String questionFont;
    String textFont;
    String categoryId;
    String categoryName;
    List<FormPageResponse> pages;
    
    // Sharing settings
    List<SharingSettingsResponse.CollaboratorResponse> collaborators;
    SharingSettingsResponse.AccessSettings editorAccess;
    SharingSettingsResponse.AccessSettings responderAccess;
    boolean editorsCanShare;
}
