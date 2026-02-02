package com.dospace.dosurvey.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;
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
