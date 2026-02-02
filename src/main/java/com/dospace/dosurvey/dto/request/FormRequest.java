package com.dospace.dosurvey.dto.request;

import com.dospace.dosurvey.entity.enums.AccessPermission;
import com.dospace.dosurvey.entity.enums.FormCloseMode;
import com.dospace.dosurvey.entity.enums.FormFor;
import com.dospace.dosurvey.entity.enums.FormStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FormRequest {
    @NotBlank(message = "Title is required")
    String title;

    String description;

    String categoryId;

    @NotNull(message = "Status is required")
    FormStatus status;

    @NotNull(message = "Close mode is required")
    FormCloseMode closeMode;

    @NotNull(message = "Form purpose is required")
    FormFor formFor;

    Integer maxResponse;

    Boolean autoThanks;

    String thankYouMessage;

    LocalDateTime validFrom;

    LocalDateTime validThrough;

    String logoImage;

    String bannerImage;

    String primaryColor;

    String backgroundColor;

    String headerFont;

    String questionFont;

    String textFont;

    AccessPermission editorAccess;

    AccessPermission responderAccess;

    Boolean editorsCanShare;

    String groupId;

    @Valid
    @NotNull(message = "Pages are required")
    List<FormPageRequest> pages;

    List<CollaboratorDto> collaborators;
}
