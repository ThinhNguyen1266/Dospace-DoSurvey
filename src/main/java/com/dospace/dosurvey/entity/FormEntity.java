package com.dospace.dosurvey.entity;

import com.dospace.dosurvey.entity.enums.*;
import com.dospace.dosurvey.utils.NanoIdUtilsCustom;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
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
@Table(name = "form")
public class FormEntity extends BaseAuditableEntity {
    @Id
    @Column(name = "id", nullable = false, length = 12)
    String id;

    @Column(name = "tenant_id", length = 12, updatable = false)
    String tenantId;

    // Changed from ManyToOne AccountEntity to String ownerId
    @Column(name = "owner_id", nullable = false, length = 12)
    String ownerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "group_id")
    FormGroupEntity group;

    @Column(name = "title", nullable = false)
    String title;

    @Column(name = "description")
    String description;

    @Column(name = "public_link", nullable = false, unique = true)
    String publicLink;

    @Column(name = "editor_link", unique = true)
    String editorLink;

    @Column(name = "qr_url")
    String qrUrl;

    @Column(name = "logo_image")
    String logoImage;

    @Column(name = "banner_image")
    String bannerImage;

    @Column(name = "primary_color", length = 7)
    String primaryColor;

    @Column(name = "background_color", length = 7)
    String backgroundColor;

    @Column(name = "header_font")
    String headerFont;

    @Column(name = "question_font")
    String questionFont;

    @Column(name = "text_font")
    String textFont;

    @ColumnDefault("false")
    @Column(name = "ai_accept", nullable = false)
    Boolean aiAccept;

    @Column(name = "ai_response", columnDefinition = "TEXT")
    String aiResponse;

    @Enumerated(EnumType.STRING)
    @Column(name = "responder_access", nullable = false)
    @ColumnDefault("'RESTRICTED'")
    AccessPermission responderAccess;

    @Enumerated(EnumType.STRING)
    @Column(name = "editor_access", nullable = false)
    @ColumnDefault("'RESTRICTED'")
    AccessPermission editorAccess;

    @Column(name = "editors_can_share", nullable = false)
    @ColumnDefault("false")
    Boolean editorsCanShare;

    @OneToMany(mappedBy = "form", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    Set<FormCollaboratorEntity> collaborators = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "category_id")
    FormCategoryEntity category;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    FormStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "delete_status", nullable = false)
    @Builder.Default
    DeleteStatus deleteStatus = DeleteStatus.ACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(name = "close_mode", nullable = false)
    FormCloseMode closeMode;

    @Column(name = "max_response")
    Integer maxResponse;

    @ColumnDefault("false")
    @Column(name = "auto_thanks", nullable = false)
    Boolean autoThanks;

    @Column(name = "thank_you_message", columnDefinition = "TEXT")
    String thankYouMessage;

    @Column(name = "valid_from")
    LocalDateTime validFrom;

    @Column(name = "valid_through")
    LocalDateTime validThrough;

    @OneToMany(mappedBy = "form", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("order ASC")
    @Builder.Default
    List<FormPageEntity> pages = new ArrayList<>();

    @OneToMany(mappedBy = "form")
    @Builder.Default
    List<FormResponseEntity> responses = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "form_for", nullable = false)
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
