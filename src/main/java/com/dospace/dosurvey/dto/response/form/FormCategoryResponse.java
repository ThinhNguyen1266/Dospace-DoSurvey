package com.dospace.dosurvey.dto.response.form;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;

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
    Timestamp createdAt;
    Timestamp updatedAt;
}
