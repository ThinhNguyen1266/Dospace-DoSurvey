package com.dospace.dosurvey.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FormPageResponse {
    String id;
    String title;
    String description;
    Integer order;
    List<FormQuestionResponse> questions;
}
