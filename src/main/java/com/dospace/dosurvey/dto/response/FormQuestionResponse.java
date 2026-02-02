package com.dospace.dosurvey.dto.response;

import com.dospace.dosurvey.entity.enums.InputType;
import com.dospace.dosurvey.entity.enums.QuestionType;
import com.dospace.dosurvey.entity.enums.RatingType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FormQuestionResponse {
    String id;
    String question;
    Boolean required;
    Integer order;
    String alias;
    InputType inputType;
    QuestionType questionType;
    RatingType ratingType;
    List<String> options;
    List<String> gridRows;
    Integer scaleCount;
    String description;
    Map<String, String> navigationConfig;
}
