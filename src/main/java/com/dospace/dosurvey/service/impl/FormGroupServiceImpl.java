package com.dospace.dosurvey.service.impl;

import com.dospace.dosurvey.context.UserContextHolder;
import com.dospace.dosurvey.dto.request.CreateGroupRequest;
import com.dospace.dosurvey.dto.request.InviteMemberRequest;
import com.dospace.dosurvey.dto.request.UpdateGroupRequest;
import com.dospace.dosurvey.dto.request.UpdateMemberRoleRequest;
import com.dospace.dosurvey.dto.response.FormResponse;
import com.dospace.dosurvey.dto.response.FormGroupMemberResponse;
import com.dospace.dosurvey.dto.response.FormGroupResponse;
import com.dospace.dosurvey.entity.FormEntity;
import com.dospace.dosurvey.entity.FormGroupEntity;
import com.dospace.dosurvey.entity.FormGroupMemberEntity;
import com.dospace.dosurvey.entity.enums.DeleteStatus;
import com.dospace.dosurvey.entity.enums.GroupRole;
import com.dospace.dosurvey.entity.enums.InvitationStatus;
import com.dospace.dosurvey.exception.AppException;
import com.dospace.dosurvey.exception.ErrorCode;
import com.dospace.dosurvey.mapper.FormMapper;
import com.dospace.dosurvey.mapper.FormGroupMapper;
import com.dospace.dosurvey.repository.FormRepository;
import com.dospace.dosurvey.repository.FormGroupMemberRepository;
import com.dospace.dosurvey.repository.FormGroupRepository;
import com.dospace.dosurvey.service.interfaces.FormGroupService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FormGroupServiceImpl implements FormGroupService {

    FormGroupRepository groupRepository;
    FormGroupMemberRepository memberRepository;
    FormRepository formRepository;
    FormGroupMapper groupMapper;
    FormMapper formMapper;

    @Override
    @Transactional
    public FormGroupResponse create(CreateGroupRequest request) {
        String currentUserId = UserContextHolder.getRequiredAccountId();
        String tenantId = UserContextHolder.getRequiredUser().getTenantId();

        FormGroupEntity group = groupMapper.fromRequestToEntity(request);
        group.setOwnerId(currentUserId);
        group.setTenantId(tenantId);

        group = groupRepository.save(group);

        // Add owner as a member with OWNER role
        FormGroupMemberEntity ownerMember = FormGroupMemberEntity.builder()
                .group(group)
                .email(UserContextHolder.getRequiredUser().getEmail())
                .accountId(currentUserId)
                .role(GroupRole.OWNER)
                .status(InvitationStatus.ACCEPTED)
                .invitedBy(currentUserId)
                .invitedAt(LocalDateTime.now())
                .acceptedAt(LocalDateTime.now())
                .build();
        memberRepository.save(ownerMember);

        return buildGroupResponse(group, List.of(ownerMember));
    }

    @Override
    @Transactional(readOnly = true)
    public FormGroupResponse getById(String id) {
        FormGroupEntity group = getGroupById(id);
        checkGroupAccess(group);

        List<FormGroupMemberEntity> members = memberRepository.findByGroupId(id);
        return buildGroupResponse(group, members);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FormGroupResponse> getMyGroups(Pageable pageable) {
        String currentUserId = UserContextHolder.getRequiredAccountId();

        Page<FormGroupEntity> groups = groupRepository.findAllGroupsByAccountId(
                currentUserId, DeleteStatus.ACTIVE, pageable);

        return groups.map(group -> {
            List<FormGroupMemberEntity> members = memberRepository.findByGroupIdAndStatus(
                    group.getId(), InvitationStatus.ACCEPTED);
            return buildGroupResponse(group, members);
        });
    }

    @Override
    @Transactional
    public FormGroupResponse update(String id, UpdateGroupRequest request) {
        FormGroupEntity group = getGroupById(id);
        checkGroupManageAccess(group);

        if (request.getName() != null) {
            group.setName(request.getName());
        }
        if (request.getDescription() != null) {
            group.setDescription(request.getDescription());
        }

        group = groupRepository.save(group);
        List<FormGroupMemberEntity> members = memberRepository.findByGroupId(id);
        return buildGroupResponse(group, members);
    }

    @Override
    @Transactional
    public void delete(String id) {
        FormGroupEntity group = getGroupById(id);

        String currentUserId = UserContextHolder.getRequiredAccountId();
        if (!group.getOwnerId().equals(currentUserId)) {
            throw new AppException(ErrorCode.GROUP_ACCESS_DENIED);
        }

        group.setDeleteStatus(DeleteStatus.DELETED);
        groupRepository.save(group);
    }

    @Override
    @Transactional
    public List<FormGroupMemberResponse> inviteMembers(String groupId, InviteMemberRequest request) {
        FormGroupEntity group = getGroupById(groupId);
        checkGroupManageAccess(group);

        String currentUserId = UserContextHolder.getRequiredAccountId();
        List<FormGroupMemberEntity> invitedMembers = new ArrayList<>();

        for (String email : request.getEmails()) {
            if (memberRepository.existsByGroupIdAndEmail(groupId, email)) {
                log.info("User {} is already a member of group {}", email, groupId);
                continue;
            }

            FormGroupMemberEntity member = FormGroupMemberEntity.builder()
                    .group(group)
                    .email(email)
                    .role(request.getRole() != null ? request.getRole() : GroupRole.MEMBER)
                    .status(InvitationStatus.PENDING)
                    .invitedBy(currentUserId)
                    .invitedAt(LocalDateTime.now())
                    .build();

            invitedMembers.add(memberRepository.save(member));
        }

        return groupMapper.fromMemberEntitiesToResponses(invitedMembers);
    }

    @Override
    @Transactional
    public FormGroupMemberResponse acceptInvitation(String groupId) {
        String currentEmail = UserContextHolder.getRequiredUser().getEmail();
        String currentUserId = UserContextHolder.getRequiredAccountId();

        FormGroupMemberEntity member = memberRepository.findByGroupIdAndEmail(groupId, currentEmail)
                .orElseThrow(() -> new AppException(ErrorCode.INVITATION_NOT_FOUND));

        if (member.getStatus() != InvitationStatus.PENDING) {
            throw new AppException(ErrorCode.INVITATION_NOT_FOUND);
        }

        member.setStatus(InvitationStatus.ACCEPTED);
        member.setAccountId(currentUserId);
        member.setAcceptedAt(LocalDateTime.now());

        member = memberRepository.save(member);
        return groupMapper.fromMemberEntityToResponse(member);
    }

    @Override
    @Transactional
    public void declineInvitation(String groupId) {
        String currentEmail = UserContextHolder.getRequiredUser().getEmail();

        FormGroupMemberEntity member = memberRepository.findByGroupIdAndEmail(groupId, currentEmail)
                .orElseThrow(() -> new AppException(ErrorCode.INVITATION_NOT_FOUND));

        if (member.getStatus() != InvitationStatus.PENDING) {
            throw new AppException(ErrorCode.INVITATION_NOT_FOUND);
        }

        member.setStatus(InvitationStatus.DECLINED);
        memberRepository.save(member);
    }

    @Override
    @Transactional
    public void removeMember(String groupId, String memberId) {
        FormGroupEntity group = getGroupById(groupId);
        checkGroupManageAccess(group);

        FormGroupMemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_GROUP_MEMBER));

        if (!member.getGroup().getId().equals(groupId)) {
            throw new AppException(ErrorCode.NOT_GROUP_MEMBER);
        }

        if (member.getRole() == GroupRole.OWNER) {
            throw new AppException(ErrorCode.CANNOT_REMOVE_OWNER);
        }

        member.setStatus(InvitationStatus.REMOVED);
        memberRepository.save(member);
    }

    @Override
    @Transactional
    public FormGroupMemberResponse updateMemberRole(String groupId, String memberId, UpdateMemberRoleRequest request) {
        FormGroupEntity group = getGroupById(groupId);

        String currentUserId = UserContextHolder.getRequiredAccountId();
        if (!group.getOwnerId().equals(currentUserId)) {
            throw new AppException(ErrorCode.GROUP_ACCESS_DENIED);
        }

        FormGroupMemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_GROUP_MEMBER));

        if (!member.getGroup().getId().equals(groupId)) {
            throw new AppException(ErrorCode.NOT_GROUP_MEMBER);
        }

        if (member.getRole() == GroupRole.OWNER) {
            throw new AppException(ErrorCode.CANNOT_CHANGE_OWNER_ROLE);
        }

        member.setRole(request.getRole());
        member = memberRepository.save(member);
        return groupMapper.fromMemberEntityToResponse(member);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FormGroupMemberResponse> getMembers(String groupId) {
        FormGroupEntity group = getGroupById(groupId);
        checkGroupAccess(group);

        List<FormGroupMemberEntity> members = memberRepository.findByGroupId(groupId);
        return groupMapper.fromMemberEntitiesToResponses(members);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FormResponse> getGroupForms(String groupId, Pageable pageable) {
        FormGroupEntity group = getGroupById(groupId);
        checkGroupAccess(group);

        Page<FormEntity> forms = formRepository.findByGroupIdAndDeleteStatus(groupId, DeleteStatus.ACTIVE, pageable);
        return forms.map(formMapper::fromEntityToResponse);
    }

    // Helper methods
    private FormGroupEntity getGroupById(String id) {
        return groupRepository.findByIdAndDeleteStatus(id, DeleteStatus.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.GROUP_NOT_FOUND));
    }

    private void checkGroupAccess(FormGroupEntity group) {
        String currentUserId = UserContextHolder.getRequiredAccountId();
        String currentEmail = UserContextHolder.getRequiredUser().getEmail();

        // Owner always has access
        if (group.getOwnerId().equals(currentUserId)) {
            return;
        }

        // Check if user is an accepted member
        boolean isMember = memberRepository.isActiveMember(group.getId(), currentEmail, currentUserId);
        if (!isMember) {
            throw new AppException(ErrorCode.GROUP_ACCESS_DENIED);
        }
    }

    private void checkGroupManageAccess(FormGroupEntity group) {
        String currentUserId = UserContextHolder.getRequiredAccountId();
        String currentEmail = UserContextHolder.getRequiredUser().getEmail();

        // Owner always has access
        if (group.getOwnerId().equals(currentUserId)) {
            return;
        }

        // Check if user is an accepted admin member
        FormGroupMemberEntity member = memberRepository.findMemberByGroupAndEmailOrAccountId(
                group.getId(), currentEmail, currentUserId, InvitationStatus.ACCEPTED)
                .orElseThrow(() -> new AppException(ErrorCode.GROUP_ACCESS_DENIED));

        if (!member.canManageMembers()) {
            throw new AppException(ErrorCode.GROUP_ACCESS_DENIED);
        }
    }

    private FormGroupResponse buildGroupResponse(FormGroupEntity group, List<FormGroupMemberEntity> members) {
        FormGroupResponse response = groupMapper.fromEntityToResponse(group);
        response.setMemberCount((int) members.stream().filter(FormGroupMemberEntity::isAccepted).count());
        response.setFormCount(group.getForms() != null ? group.getForms().size() : 0);
        response.setMembers(groupMapper.fromMemberEntitiesToResponses(members));
        return response;
    }
}
