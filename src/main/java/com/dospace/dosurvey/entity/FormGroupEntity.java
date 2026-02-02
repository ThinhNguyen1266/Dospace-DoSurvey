package com.dospace.dosurvey.entity;

import com.dospace.dosurvey.entity.enums.DeleteStatus;
import com.dospace.dosurvey.utils.NanoIdUtilsCustom;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "form_group")
public class FormGroupEntity extends BaseAuditableEntity {
    @Id
    @Column(name = "id", nullable = false, length = 12)
    String id;

    @Column(name = "tenant_id", length = 12, updatable = false)
    String tenantId;

    @Column(name = "name", nullable = false, length = 128)
    String name;

    @Column(name = "description", length = 512)
    String description;

    @Column(name = "owner_id", nullable = false, length = 12)
    String ownerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "delete_status", nullable = false)
    @ColumnDefault("'ACTIVE'")
    @Builder.Default
    DeleteStatus deleteStatus = DeleteStatus.ACTIVE;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    Set<FormGroupMemberEntity> members = new HashSet<>();

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
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
    }

    public boolean isActive() {
        return deleteStatus == DeleteStatus.ACTIVE;
    }
}
