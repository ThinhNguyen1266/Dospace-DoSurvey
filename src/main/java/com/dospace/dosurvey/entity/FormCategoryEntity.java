package com.dospace.dosurvey.entity;

import com.dospace.dosurvey.entity.enums.DeleteStatus;
import com.dospace.dosurvey.entity.enums.NormalStatus;
import com.dospace.dosurvey.utils.NanoIdUtilsCustom;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "form_category")
public class FormCategoryEntity extends BaseAuditableEntity {
    @Id
    @Column(name = "id", nullable = false, length = 12)
    String id;

    @NotNull
    @Column(name = "name", nullable = false, length = 64)
    String name;

    @Column(name = "tenant_id", length = 12, updatable = false)
    String tenantId;

    @Column(name = "description")
    String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    NormalStatus status = NormalStatus.ACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(name = "delete_status", nullable = false)
    @Builder.Default
    DeleteStatus deleteStatus = DeleteStatus.ACTIVE;

    @OneToMany(mappedBy = "category")
    @Builder.Default
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
