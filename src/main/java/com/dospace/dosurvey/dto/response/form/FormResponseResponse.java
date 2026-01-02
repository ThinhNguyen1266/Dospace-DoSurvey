package com.dospace.dosurvey.dto.response.form;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
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
    Timestamp createdAt;
    List<FormResponseQuestionResponse> answers;
}
