package com.dospace.dosurvey.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FormGroupResponse {
    String id;
    String tenantId;
    String name;
    String description;
    String ownerId;
    Integer memberCount;
    Integer formCount;
    List<FormGroupMemberResponse> members;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
