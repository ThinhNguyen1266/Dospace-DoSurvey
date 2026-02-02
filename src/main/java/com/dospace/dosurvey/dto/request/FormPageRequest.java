package com.dospace.dosurvey.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FormPageRequest {
    String id;

    @NotBlank(message = "Page title is required")
    String title;

    String description;

    Integer order;

    @Valid
    List<FormQuestionRequest> questions;
}
