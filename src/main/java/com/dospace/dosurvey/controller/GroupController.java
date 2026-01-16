package com.dospace.dosurvey.controller;

import com.dospace.dosurvey.dto.APIResponse;
import com.dospace.dosurvey.dto.request.CreateGroupRequest;
import com.dospace.dosurvey.dto.request.InviteMemberRequest;
import com.dospace.dosurvey.dto.request.UpdateGroupRequest;
import com.dospace.dosurvey.dto.request.UpdateMemberRoleRequest;
import com.dospace.dosurvey.dto.response.FormResponse;
import com.dospace.dosurvey.dto.response.FormGroupMemberResponse;
import com.dospace.dosurvey.dto.response.FormGroupResponse;
import com.dospace.dosurvey.service.interfaces.FormGroupService;
import com.dospace.dosurvey.utils.PaginationUtil;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GroupController {

    FormGroupService groupService;

    @PostMapping
    public APIResponse<FormGroupResponse> create(@Valid @RequestBody CreateGroupRequest request) {
        return APIResponse.success(groupService.create(request), "Group created successfully");
    }

    @GetMapping("/{id}")
    public APIResponse<FormGroupResponse> getById(@PathVariable String id) {
        return APIResponse.success(groupService.getById(id));
    }

    @GetMapping
    public APIResponse<Page<FormGroupResponse>> getMyGroups(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PaginationUtil.createPageable(page, size, Sort.by("createdAt").descending());
        return APIResponse.success(groupService.getMyGroups(pageable));
    }

    @PutMapping("/{id}")
    public APIResponse<FormGroupResponse> update(
            @PathVariable String id,
            @Valid @RequestBody UpdateGroupRequest request
    ) {
        return APIResponse.success(groupService.update(id, request), "Group updated successfully");
    }

    @DeleteMapping("/{id}")
    public APIResponse<Void> delete(@PathVariable String id) {
        groupService.delete(id);
        return APIResponse.success(null, "Group deleted successfully");
    }

    @PostMapping("/{id}/invite")
    public APIResponse<List<FormGroupMemberResponse>> inviteMembers(
            @PathVariable String id,
            @Valid @RequestBody InviteMemberRequest request
    ) {
        return APIResponse.success(groupService.inviteMembers(id, request), "Invitations sent successfully");
    }

    @PostMapping("/{id}/accept")
    public APIResponse<FormGroupMemberResponse> acceptInvitation(@PathVariable String id) {
        return APIResponse.success(groupService.acceptInvitation(id), "Invitation accepted");
    }

    @PostMapping("/{id}/decline")
    public APIResponse<Void> declineInvitation(@PathVariable String id) {
        groupService.declineInvitation(id);
        return APIResponse.success(null, "Invitation declined");
    }

    @GetMapping("/{id}/members")
    public APIResponse<List<FormGroupMemberResponse>> getMembers(@PathVariable String id) {
        return APIResponse.success(groupService.getMembers(id));
    }

    @DeleteMapping("/{id}/members/{memberId}")
    public APIResponse<Void> removeMember(
            @PathVariable String id,
            @PathVariable String memberId
    ) {
        groupService.removeMember(id, memberId);
        return APIResponse.success(null, "Member removed successfully");
    }

    @PutMapping("/{id}/members/{memberId}/role")
    public APIResponse<FormGroupMemberResponse> updateMemberRole(
            @PathVariable String id,
            @PathVariable String memberId,
            @Valid @RequestBody UpdateMemberRoleRequest request
    ) {
        return APIResponse.success(groupService.updateMemberRole(id, memberId, request), "Role updated successfully");
    }

    @GetMapping("/{id}/forms")
    public APIResponse<Page<FormResponse>> getGroupForms(
            @PathVariable String id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PaginationUtil.createPageable(page, size, Sort.by("createdAt").descending());
        return APIResponse.success(groupService.getGroupForms(id, pageable));
    }
}
