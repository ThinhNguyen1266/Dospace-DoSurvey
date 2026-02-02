package com.dospace.dosurvey.dto.request;

import com.dospace.dosurvey.entity.enums.GroupRole;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
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
