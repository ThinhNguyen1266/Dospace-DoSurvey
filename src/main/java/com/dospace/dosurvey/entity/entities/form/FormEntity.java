package com.dospace.dosurvey.entity.entities.form;

import com.dospace.dosurvey.entity.enums.AccessPermission;
import com.dospace.dosurvey.entity.enums.DeleteStatus;
import com.dospace.dosurvey.entity.enums.FormCloseMode;
import com.dospace.dosurvey.entity.enums.FormFor;
import com.dospace.dosurvey.entity.enums.FormStatus;
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
import jakarta.persistence.OrderBy;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "form")
public class FormEntity {
    @Id
    @Column(name = "fom_id", nullable = false, length = 12)
    String id;

    @Column(name = "fom_tenant_id", length = 12, updatable = false)
    String tenantId;

    // Changed from ManyToOne AccountEntity to String ownerId
    @Column(name = "fom_owner_id", nullable = false, length = 12)
    String ownerId;

    @Column(name = "fom_title", nullable = false)
    String title;

    @Column(name = "fom_description")
    String description;

    @Column(name = "fom_public_link", nullable = false, unique = true)
    String publicLink;

    @Column(name = "fom_editor_link", unique = true)
    String editorLink;

    @Column(name = "fom_qr_url")
    String qrUrl;

    @Column(name = "fom_logo_image")
    String logoImage;

    @Column(name = "fom_banner_image")
    String bannerImage;

    @Column(name = "fom_primary_color", length = 7)
    String primaryColor;

    @Column(name = "fom_background_color", length = 7)
    String backgroundColor;

    @Column(name = "fom_header_font")
    String headerFont;

    @Column(name = "fom_question_font")
    String questionFont;

    @Column(name = "fom_text_font")
    String textFont;

    @ColumnDefault("false")
    @Column(name = "fom_ai_accept", nullable = false)
    Boolean aiAccept;

    @Column(name = "fom_ai_response", columnDefinition = "TEXT")
    String aiResponse;

    @Enumerated(EnumType.STRING)
    @Column(name = "fom_responder_access", nullable = false)
    @ColumnDefault("'RESTRICTED'")
    AccessPermission responderAccess;

    @Enumerated(EnumType.STRING)
    @Column(name = "fom_editor_access", nullable = false)
    @ColumnDefault("'RESTRICTED'")
    AccessPermission editorAccess;

    @Column(name = "fom_editors_can_share", nullable = false)
    @ColumnDefault("false")
    Boolean editorsCanShare;

    @OneToMany(mappedBy = "form", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    Set<FormCollaboratorEntity> collaborators = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "fom_category_id")
    FormCategoryEntity category;

    @Enumerated(EnumType.STRING)
    @Column(name = "fom_status", nullable = false)
    FormStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "fom_delete_status", nullable = false)
    DeleteStatus deleteStatus = DeleteStatus.ACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(name = "fom_close_mode", nullable = false)
    FormCloseMode closeMode;

    @Column(name = "fom_max_response")
    Integer maxResponse;

    @ColumnDefault("false")
    @Column(name = "fom_auto_thanks", nullable = false)
    Boolean autoThanks;

    @Column(name = "fom_valid_from")
    LocalDateTime validFrom;

    @Column(name = "fom_valid_through")
    LocalDateTime validThrough;

    @CreatedBy
    @Column(name = "fom_created_by", length = 12, updatable = false)
    String createdBy;

    @CreatedDate
    @Column(name = "fom_created_at", updatable = false)
    Timestamp createdAt;

    @LastModifiedBy
    @Column(name = "fom_updated_by", length = 12, insertable = false)
    String updatedBy;

    @LastModifiedDate
    @Column(name = "fom_updated_at", insertable = false)
    Timestamp updatedAt;

    @OneToMany(mappedBy = "form", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("order ASC")
    List<FormPageEntity> pages = new ArrayList<>();

    @OneToMany(mappedBy = "form")
    List<FormResponseEntity> responses = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "fom_form_for", nullable = false)
    FormFor formFor;

    @PrePersist
    public void prePersist() {
        if (this.id == null || this.id.isEmpty()) {
            this.id = NanoIdUtilsCustom.genNanoIdLimit12();
        }
        if (this.deleteStatus == null) {
            this.deleteStatus = DeleteStatus.ACTIVE;
        }

        if (this.responderAccess == null) {
            this.responderAccess = AccessPermission.ANYONE_WITH_LINK;
        }

        if (this.editorAccess == null) {
            this.editorAccess = AccessPermission.RESTRICTED;
        }

        if (this.editorsCanShare == null) {
            this.editorsCanShare = Boolean.FALSE;
        }

        if (this.aiAccept == null) {
            this.aiAccept = Boolean.FALSE;
        }

        if (this.autoThanks == null) {
            this.autoThanks = Boolean.FALSE;
        }
    }

    public boolean isPublic() {
        return isActive() && status == FormStatus.PUBLIC;
    }

    public boolean isPrivate() {
        return isActive() && status == FormStatus.PRIVATE;
    }

    public boolean isDraft() {
        return isActive() && status == FormStatus.DRAFT;
    }

    public boolean isActive() {
        return deleteStatus == DeleteStatus.ACTIVE;
    }
}
