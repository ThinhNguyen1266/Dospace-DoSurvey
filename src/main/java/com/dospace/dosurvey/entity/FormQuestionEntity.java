package com.dospace.dosurvey.entity;

import com.dospace.dosurvey.entity.enums.InputType;
import com.dospace.dosurvey.entity.enums.QuestionType;
import com.dospace.dosurvey.entity.enums.RatingType;
import com.dospace.dosurvey.utils.NanoIdUtilsCustom;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Entity
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "form_question")
public class FormQuestionEntity extends BaseAuditableEntity {
    @Id
    @Column(name = "id", nullable = false, length = 12)
    String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "page_id", nullable = false)
    FormPageEntity page;

    @Column(name = "question", nullable = false)
    String question;

    @Column(name = "required", nullable = false)
    Boolean required;

    @Column(name = "order", nullable = false)
    Integer order;

    @Column(name = "alias", nullable = false)
    String alias;

    @Enumerated(EnumType.STRING)
    @Column(name = "input_type", nullable = false)
    InputType inputType;

    @Enumerated(EnumType.STRING)
    @Column(name = "question_type", nullable = false)
    QuestionType questionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "rating_type")
    RatingType ratingType;

    @Column(name = "navigation_config", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    Map<String, String> navigationConfig;

    @Column(name = "options", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    List<String> options;

    @Column(name = "grid_rows", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    List<String> gridRows;

    @Column(name = "scale_count")
    Integer scaleCount;

    @Column(name = "description")
    String description;

    @PrePersist
    public void prePersist() {
        if (this.id == null || this.id.isEmpty()) {
            this.id = NanoIdUtilsCustom.genNanoIdLimit12();
        }
    }
}
