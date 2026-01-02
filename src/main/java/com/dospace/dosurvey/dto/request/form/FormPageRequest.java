package com.dospace.dosurvey.dto.request.form;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
public class FormPageRequest {
    String id;

    @NotBlank(message = "Page title is required")
    String title;

    String description;

    Integer order;

    @Valid
    List<FormQuestionRequest> questions;
}
