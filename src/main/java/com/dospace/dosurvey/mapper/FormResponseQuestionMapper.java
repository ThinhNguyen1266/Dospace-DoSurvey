package com.dospace.dosurvey.mapper;

import com.dospace.dosurvey.dto.request.FormResponseQuestionRequest;
import com.dospace.dosurvey.dto.response.FormResponseQuestionResponse;
import com.dospace.dosurvey.entity.FormResponseQuestionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FormResponseQuestionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "response", ignore = true)
    @Mapping(target = "question", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    FormResponseQuestionEntity fromRequestToEntity(FormResponseQuestionRequest request);

    @Mapping(target = "questionId", source = "question.id")
    @Mapping(target = "question", source = "question.question")
    FormResponseQuestionResponse fromEntityToResponse(FormResponseQuestionEntity entity);

    List<FormResponseQuestionResponse> fromEntitiesToResponses(List<FormResponseQuestionEntity> entities);
}
