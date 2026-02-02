package com.dospace.dosurvey.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FormResponseQuestionRequest {
    @NotBlank(message = "Question ID is required")
    String questionId;

    @NotNull(message = "Answer is required")
    List<String> answer;
}
