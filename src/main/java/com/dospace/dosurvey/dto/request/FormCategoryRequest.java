package com.dospace.dosurvey.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FormCategoryRequest {
    @NotBlank(message = "Category name is required")
    String name;

    String description;
}
