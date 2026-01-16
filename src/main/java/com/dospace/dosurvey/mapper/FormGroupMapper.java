package com.dospace.dosurvey.mapper;

import com.dospace.dosurvey.dto.request.CreateGroupRequest;
import com.dospace.dosurvey.dto.response.FormGroupMemberResponse;
import com.dospace.dosurvey.dto.response.FormGroupResponse;
import com.dospace.dosurvey.entity.FormGroupEntity;
import com.dospace.dosurvey.entity.FormGroupMemberEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FormGroupMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "ownerId", ignore = true)
    @Mapping(target = "deleteStatus", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "members", ignore = true)
    @Mapping(target = "forms", ignore = true)
    FormGroupEntity fromRequestToEntity(CreateGroupRequest request);

    @Mapping(target = "memberCount", ignore = true)
    @Mapping(target = "formCount", ignore = true)
    @Mapping(target = "members", ignore = true)
    FormGroupResponse fromEntityToResponse(FormGroupEntity entity);

    @Mapping(target = "groupId", source = "group.id")
    FormGroupMemberResponse fromMemberEntityToResponse(FormGroupMemberEntity entity);

    List<FormGroupMemberResponse> fromMemberEntitiesToResponses(List<FormGroupMemberEntity> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "ownerId", ignore = true)
    @Mapping(target = "deleteStatus", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "members", ignore = true)
    @Mapping(target = "forms", ignore = true)
    void updateFromRequestToEntity(CreateGroupRequest request, @MappingTarget FormGroupEntity entity);
}
