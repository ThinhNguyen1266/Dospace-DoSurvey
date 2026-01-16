package com.dospace.dosurvey.dto.response;

import com.dospace.dosurvey.entity.enums.GroupRole;
import com.dospace.dosurvey.entity.enums.InvitationStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FormGroupMemberResponse {
    String id;
    String groupId;
    String email;
    String accountId;
    GroupRole role;
    InvitationStatus status;
    String invitedBy;
    LocalDateTime invitedAt;
    LocalDateTime acceptedAt;
}
