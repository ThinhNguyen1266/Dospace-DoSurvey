package com.dospace.dosurvey.mapper;

import com.dospace.dosurvey.dto.request.FormPageRequest;
import com.dospace.dosurvey.dto.request.FormQuestionRequest;
import com.dospace.dosurvey.dto.request.FormRequest;
import com.dospace.dosurvey.dto.response.*;
import com.dospace.dosurvey.entity.FormEntity;
import com.dospace.dosurvey.entity.FormPageEntity;
import com.dospace.dosurvey.entity.FormQuestionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FormMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "ownerId", ignore = true)
    @Mapping(target = "publicLink", ignore = true)
    @Mapping(target = "editorLink", ignore = true)
    @Mapping(target = "qrUrl", ignore = true)
    @Mapping(target = "aiAccept", ignore = true)
    @Mapping(target = "aiResponse", ignore = true)
    @Mapping(target = "deleteStatus", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "collaborators", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "group", ignore = true)
    @Mapping(target = "responses", ignore = true)
    @Mapping(target = "pages", ignore = true)
    FormEntity fromRequestToEntity(FormRequest request);

    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "groupId", source = "group.id")
    @Mapping(target = "groupName", source = "group.name")
    FormResponse fromEntityToResponse(FormEntity entity);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    FormOptions fromEntityToOption(FormEntity entity);

    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "closeMode", source = "closeMode")
    @Mapping(target = "formFor", source = "formFor")
    @Mapping(target = "collaborators", ignore = true)
    @Mapping(target = "editorAccess", ignore = true)
    @Mapping(target = "responderAccess", ignore = true)
    @Mapping(target = "editorsCanShare", ignore = true)
    FormEditorResponse fromEntityToEditorResponse(FormEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "ownerId", ignore = true)
    @Mapping(target = "publicLink", ignore = true)
    @Mapping(target = "editorLink", ignore = true)
    @Mapping(target = "qrUrl", ignore = true)
    @Mapping(target = "aiAccept", ignore = true)
    @Mapping(target = "aiResponse", ignore = true)
    @Mapping(target = "deleteStatus", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "collaborators", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "group", ignore = true)
    @Mapping(target = "responses", ignore = true)
    @Mapping(target = "pages", ignore = true)
    void updateFromRequestToEntity(FormRequest request, @MappingTarget FormEntity entity);

    // Page mappings
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "form", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "questions", ignore = true)
    FormPageEntity fromPageRequestToEntity(FormPageRequest request);

    FormPageResponse fromPageEntityToResponse(FormPageEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "form", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "questions", ignore = true)
    void updateFromPageRequestToEntity(FormPageRequest request, @MappingTarget FormPageEntity entity);

    // Question mappings
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "page", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    FormQuestionEntity fromQuestionRequestToEntity(FormQuestionRequest request);

    FormQuestionResponse fromQuestionEntityToResponse(FormQuestionEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "page", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateFromQuestionRequestToEntity(FormQuestionRequest request, @MappingTarget FormQuestionEntity entity);

    List<FormPageResponse> fromPageEntitiesToResponses(List<FormPageEntity> entities);

    List<FormQuestionResponse> fromQuestionEntitiesToResponses(List<FormQuestionEntity> entities);
}
