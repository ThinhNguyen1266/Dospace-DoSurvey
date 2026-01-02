package com.dospace.dosurvey.entity.entities.form;

import com.dospace.dosurvey.entity.enums.InputType;
import com.dospace.dosurvey.entity.enums.QuestionType;
import com.dospace.dosurvey.entity.enums.RatingType;
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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "form_question")
public class FormQuestionEntity {
    @Id
    @Column(name = "fqu_id", nullable = false, length = 12)
    String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "fqu_page_id", nullable = false)
    FormPageEntity page;

    @Column(name = "fqu_question", nullable = false)
    String question;

    @Column(name = "fqu_required", nullable = false)
    Boolean required;

    @Column(name = "fqu_order", nullable = false)
    Integer order;

    @Column(name = "fqu_alias", nullable = false)
    String alias;

    @Enumerated(EnumType.STRING)
    @Column(name = "fqu_input_type", nullable = false)
    InputType inputType;

    @Enumerated(EnumType.STRING)
    @Column(name = "fqu_question_type", nullable = false)
    QuestionType questionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "fqu_rating_type")
    RatingType ratingType;

    @Column(name = "fqu_navigation_config", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    Map<String, String> navigationConfig;

    @Column(name = "fqu_options", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    List<String> options;

    @Column(name = "fqu_scale_count")
    Integer scaleCount;

    @Column(name = "fqu_description")
    String description;

    @CreatedBy
    @Column(name = "fqu_created_by", length = 12, updatable = false)
    String createdBy;

    @CreatedDate
    @Column(name = "fqu_created_at", updatable = false)
    Timestamp createdAt;

    @LastModifiedBy
    @Column(name = "fqu_updated_by", length = 12, insertable = false)
    String updatedBy;

    @LastModifiedDate
    @Column(name = "fqu_updated_at", insertable = false)
    Timestamp updatedAt;

    // Removed: @OneToMany(mappedBy = "question") List<TicketCheckinConfigEntity> ticketCheckinConfigs
    // This relationship is outside the survey module scope

    @PrePersist
    public void prePersist() {
        if (this.id == null || this.id.isEmpty()) {
            this.id = NanoIdUtilsCustom.genNanoIdLimit12();
        }
    }
}
