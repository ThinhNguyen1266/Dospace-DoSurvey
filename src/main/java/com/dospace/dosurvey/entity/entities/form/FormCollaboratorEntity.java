package com.dospace.dosurvey.entity.entities.form;

import com.dospace.dosurvey.entity.enums.FormRole;
import com.dospace.dosurvey.utils.NanoIdUtilsCustom;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
    @Column(name = "fco_id", nullable = false, length = 12)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fco_form_id", nullable = false)
    private FormEntity form;

    // Changed from ManyToOne AccountEntity to String accountId
    @Column(name = "fco_account_id", length = 12)
    private String accountId;

    @Column(name = "fco_email", nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "fco_role", nullable = false)
    private FormRole role;

    @Column(name = "fco_expires_at")
    private Timestamp expiresAt;

    @CreatedBy
    @Column(name = "fco_created_by", length = 12, updatable = false)
    private String createdBy;

    @CreatedDate
    @Column(name = "fco_created_at", updatable = false)
    private Timestamp createdAt;

    @LastModifiedBy
    @Column(name = "fco_updated_by", length = 12, insertable = false)
    private String updatedBy;

    @LastModifiedDate
    @Column(name = "fco_updated_at", insertable = false)
    private Timestamp updatedAt;

    @PrePersist
    public void prePersist() {
        if (this.id == null || this.id.isEmpty()) {
            this.id = NanoIdUtilsCustom.genNanoIdLimit12();
        }
    }
}
