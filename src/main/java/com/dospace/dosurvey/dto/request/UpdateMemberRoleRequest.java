package com.dospace.dosurvey.dto.request;

import com.dospace.dosurvey.entity.enums.GroupRole;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateMemberRoleRequest {
    @NotNull(message = "Role is required")
    GroupRole role;
}
