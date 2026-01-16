package com.dospace.dosurvey.dto.request;

import com.dospace.dosurvey.entity.enums.GroupRole;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InviteMemberRequest {
    @NotEmpty(message = "At least one email is required")
    List<String> emails;

    @Builder.Default
    GroupRole role = GroupRole.MEMBER;
}
