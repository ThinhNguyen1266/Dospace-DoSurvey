package com.dospace.dosurvey.entity.entities.form;

import com.dospace.dosurvey.entity.enums.DeleteStatus;
import com.dospace.dosurvey.entity.enums.NormalStatus;
import com.dospace.dosurvey.utils.NanoIdUtilsCustom;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "form_category")
public class FormCategoryEntity {
    @Id
    @Column(name = "foc_id", nullable = false, length = 12)
    String id;

    @NotNull
    @Column(name = "foc_name", nullable = false, length = 64)
    String name;

    @Column(name = "foc_tenant_id", length = 12, updatable = false)
    String tenantId;

    @Column(name = "foc_description")
    String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "foc_status", nullable = false)
    NormalStatus status = NormalStatus.ACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(name = "foc_delete_status", nullable = false)
    DeleteStatus deleteStatus = DeleteStatus.ACTIVE;

    @CreatedBy
    @Column(name = "foc_created_by", length = 12, updatable = false)
    String createdBy;

    @CreatedDate
    @Column(name = "foc_created_at", updatable = false)
    Timestamp createdAt;

    @LastModifiedBy
    @Column(name = "foc_updated_by", length = 12, insertable = false)
    String updatedBy;

    @LastModifiedDate
    @Column(name = "foc_updated_at", insertable = false)
    Timestamp updatedAt;

    @OneToMany(mappedBy = "category")
    List<FormEntity> forms = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (this.id == null || this.id.isEmpty()) {
            this.id = NanoIdUtilsCustom.genNanoIdLimit12();
        }
        if (this.deleteStatus == null) {
            this.deleteStatus = DeleteStatus.ACTIVE;
        }
        if (this.status == null) {
            this.status = NormalStatus.ACTIVE;
        }
    }

    public boolean isActive() {
        return this.status == NormalStatus.ACTIVE && this.deleteStatus == DeleteStatus.ACTIVE;
    }
}
