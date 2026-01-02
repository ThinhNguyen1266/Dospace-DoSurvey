package com.dospace.dosurvey.dto.request.form;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
public class FormResponseRequest {
    String userId;

    String bookingId;

    @Valid
    @NotNull(message = "Answers are required")
    List<FormResponseQuestionRequest> answers;
}
