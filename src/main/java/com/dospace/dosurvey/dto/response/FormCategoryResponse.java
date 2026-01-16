package com.dospace.dosurvey.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FormCategoryResponse {
    String id;
    String name;
    String description;
    String status;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
