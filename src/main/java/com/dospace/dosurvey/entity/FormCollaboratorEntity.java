package com.dospace.dosurvey.entity;

import com.dospace.dosurvey.entity.enums.FormRole;
import com.dospace.dosurvey.utils.NanoIdUtilsCustom;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "form_collaborator")
public class FormCollaboratorEntity {

    @Id
    @Column(name = "id", nullable = false, length = 12)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id", nullable = false)
    private FormEntity form;

    // Changed from ManyToOne AccountEntity to String accountId
    @Column(name = "account_id", length = 12)
    private String accountId;

    @Column(name = "email", nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private FormRole role;

    @Column(name = "expires_at")
    private Timestamp expiresAt;

    @CreatedBy
    @Column(name = "created_by", length = 12, updatable = false)
    private String createdBy;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @LastModifiedBy
    @Column(name = "updated_by", length = 12, insertable = false)
    private String updatedBy;

    @LastModifiedDate
    @Column(name = "updated_at", insertable = false)
    private Timestamp updatedAt;

    @PrePersist
    public void prePersist() {
        if (this.id == null || this.id.isEmpty()) {
            this.id = NanoIdUtilsCustom.genNanoIdLimit12();
        }
    }
}
