package com.dospace.dosurvey.entity.entities.form;

import com.dospace.dosurvey.entity.enums.DeleteStatus;
import com.dospace.dosurvey.utils.NanoIdUtilsCustom;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
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
@Table(name = "form_response")
public class FormResponseEntity {
    @Id
    @Column(name = "fre_id", nullable = false, length = 12)
    String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "fre_form_id", nullable = false)
    FormEntity form;

    // Changed from OneToOne BookingEntity to String bookingId
    @Column(name = "fre_booking_id", length = 12)
    String bookingId;

    // Changed from ManyToOne AccountEntity to String userId
    @Column(name = "fre_user_id", length = 12)
    String userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "fre_delete_status", nullable = false)
    DeleteStatus deleteStatus = DeleteStatus.ACTIVE;

    @CreatedBy
    @Column(name = "fre_created_by", length = 12, updatable = false)
    String createdBy;

    @CreatedDate
    @Column(name = "fre_created_at", updatable = false)
    Timestamp createdAt;

    @LastModifiedBy
    @Column(name = "fre_updated_by", length = 12, insertable = false)
    String updatedBy;

    @LastModifiedDate
    @Column(name = "fre_updated_at", insertable = false)
    Timestamp updatedAt;

    @OneToMany(mappedBy = "response", cascade = CascadeType.ALL, orphanRemoval = true)
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
