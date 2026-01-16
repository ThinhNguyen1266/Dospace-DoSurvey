package com.dospace.dosurvey.mapper;

import com.dospace.dosurvey.dto.request.FormCategoryRequest;
import com.dospace.dosurvey.dto.response.FormCategoryResponse;
import com.dospace.dosurvey.entity.FormCategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface FormCategoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "deleteStatus", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "forms", ignore = true)
    FormCategoryEntity fromRequestToEntity(FormCategoryRequest request);

    @Mapping(target = "status", source = "status")
    FormCategoryResponse fromEntityToResponse(FormCategoryEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "deleteStatus", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "forms", ignore = true)
    void updateFromRequestToEntity(FormCategoryRequest request, @MappingTarget FormCategoryEntity entity);
}
