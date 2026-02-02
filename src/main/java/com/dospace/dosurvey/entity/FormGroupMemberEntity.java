package com.dospace.dosurvey.entity;

import com.dospace.dosurvey.entity.enums.GroupRole;
import com.dospace.dosurvey.entity.enums.InvitationStatus;
import com.dospace.dosurvey.utils.NanoIdUtilsCustom;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "form_group_member")
public class FormGroupMemberEntity extends BaseAuditableEntity {
    @Id
    @Column(name = "id", nullable = false, length = 12)
    String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "group_id", nullable = false)
    FormGroupEntity group;

    @Column(name = "email", nullable = false)
    String email;

    @Column(name = "account_id", length = 12)
    String accountId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    @ColumnDefault("'MEMBER'")
    @Builder.Default
    GroupRole role = GroupRole.MEMBER;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @ColumnDefault("'PENDING'")
    @Builder.Default
    InvitationStatus status = InvitationStatus.PENDING;

    @Column(name = "invited_by", length = 12)
    String invitedBy;

    @Column(name = "invited_at")
    LocalDateTime invitedAt;

    @Column(name = "accepted_at")
    LocalDateTime acceptedAt;

    @PrePersist
    public void prePersist() {
        if (this.id == null || this.id.isEmpty()) {
            this.id = NanoIdUtilsCustom.genNanoIdLimit12();
        }
        if (this.role == null) {
            this.role = GroupRole.MEMBER;
        }
        if (this.status == null) {
            this.status = InvitationStatus.PENDING;
        }
        if (this.invitedAt == null) {
            this.invitedAt = LocalDateTime.now();
        }
    }

    public boolean isAccepted() {
        return status == InvitationStatus.ACCEPTED;
    }

    public boolean isPending() {
        return status == InvitationStatus.PENDING;
    }

    public boolean isOwner() {
        return role == GroupRole.OWNER;
    }

    public boolean canManageMembers() {
        return role == GroupRole.OWNER || role == GroupRole.ADMIN;
    }

    public boolean canEditForms() {
        return role == GroupRole.OWNER || role == GroupRole.ADMIN || role == GroupRole.MEMBER;
    }
}
