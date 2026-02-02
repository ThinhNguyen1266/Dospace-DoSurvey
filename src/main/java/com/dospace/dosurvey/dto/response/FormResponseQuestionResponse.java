package com.dospace.dosurvey.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FormResponseQuestionResponse {
    String id;
    String questionId;
    String question;
    List<String> answer;
}
