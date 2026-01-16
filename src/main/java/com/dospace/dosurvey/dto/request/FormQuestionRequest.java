package com.dospace.dosurvey.dto.request;

import com.dospace.dosurvey.entity.enums.InputType;
import com.dospace.dosurvey.entity.enums.QuestionType;
import com.dospace.dosurvey.entity.enums.RatingType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FormQuestionRequest {
    String id;

    @NotBlank(message = "Question text is required")
    String question;

    @NotNull(message = "Required flag is required")
    Boolean required;

    Integer order;

    String alias;

    @NotNull(message = "Input type is required")
    InputType inputType;

    @NotNull(message = "Question type is required")
    QuestionType questionType;

    RatingType ratingType;

    List<String> options;

    Integer scaleCount;

    String description;

    Map<String, String> navigationConfig;
}
