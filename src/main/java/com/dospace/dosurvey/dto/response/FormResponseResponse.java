package com.dospace.dosurvey.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FormResponseResponse {
    String id;
    String formId;
    String userId;
    String bookingId;
    LocalDateTime createdAt;
    List<FormResponseQuestionResponse> answers;
}
