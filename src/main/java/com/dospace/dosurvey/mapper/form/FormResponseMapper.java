package com.dospace.dosurvey.mapper.form;

import com.dospace.dosurvey.dto.request.form.FormResponseRequest;
import com.dospace.dosurvey.dto.response.form.FormResponseResponse;
import com.dospace.dosurvey.entity.entities.form.FormResponseEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {FormResponseQuestionMapper.class})
public interface FormResponseMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "form", ignore = true)
    @Mapping(target = "deleteStatus", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "answers", ignore = true)
    FormResponseEntity fromRequestToEntity(FormResponseRequest request);

    @Mapping(target = "formId", source = "form.id")
    @Mapping(target = "answers", source = "answers")
    FormResponseResponse fromEntityToResponse(FormResponseEntity entity);
}
