package com.dospace.dosurvey.entity;

import com.dospace.dosurvey.entity.enums.DeleteStatus;
import com.dospace.dosurvey.utils.NanoIdUtilsCustom;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "form_response")
public class FormResponseEntity extends BaseAuditableEntity {
    @Id
    @Column(name = "id", nullable = false, length = 12)
    String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "form_id", nullable = false)
    FormEntity form;

    // Changed from OneToOne BookingEntity to String bookingId
    @Column(name = "booking_id", length = 12)
    String bookingId;

    // Changed from ManyToOne AccountEntity to String userId
    @Column(name = "user_id", length = 12)
    String userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "delete_status", nullable = false)
    @Builder.Default
    DeleteStatus deleteStatus = DeleteStatus.ACTIVE;

    @OneToMany(mappedBy = "response", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    List<FormResponseQuestionEntity> answers = new ArrayList<>();

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
        return this.deleteStatus == DeleteStatus.ACTIVE;
    }
}
