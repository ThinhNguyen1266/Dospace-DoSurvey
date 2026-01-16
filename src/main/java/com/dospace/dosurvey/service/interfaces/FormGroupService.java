package com.dospace.dosurvey.service.interfaces;

import com.dospace.dosurvey.dto.request.CreateGroupRequest;
import com.dospace.dosurvey.dto.request.InviteMemberRequest;
import com.dospace.dosurvey.dto.request.UpdateGroupRequest;
import com.dospace.dosurvey.dto.request.UpdateMemberRoleRequest;
import com.dospace.dosurvey.dto.response.FormResponse;
import com.dospace.dosurvey.dto.response.FormGroupMemberResponse;
import com.dospace.dosurvey.dto.response.FormGroupResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FormGroupService {

    FormGroupResponse create(CreateGroupRequest request);

    FormGroupResponse getById(String id);

    Page<FormGroupResponse> getMyGroups(Pageable pageable);

    FormGroupResponse update(String id, UpdateGroupRequest request);

    void delete(String id);

    List<FormGroupMemberResponse> inviteMembers(String groupId, InviteMemberRequest request);

    FormGroupMemberResponse acceptInvitation(String groupId);

    void declineInvitation(String groupId);

    void removeMember(String groupId, String memberId);

    FormGroupMemberResponse updateMemberRole(String groupId, String memberId, UpdateMemberRoleRequest request);

    List<FormGroupMemberResponse> getMembers(String groupId);

    Page<FormResponse> getGroupForms(String groupId, Pageable pageable);
}
