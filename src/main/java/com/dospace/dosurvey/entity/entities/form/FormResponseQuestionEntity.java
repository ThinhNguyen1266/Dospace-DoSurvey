package com.dospace.dosurvey.entity.entities.form;

import com.dospace.dosurvey.utils.NanoIdUtilsCustom;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "form_response_question")
public class FormResponseQuestionEntity {
    @Id
    @Column(name = "frq_id", nullable = false, length = 12)
    String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "frq_response_id", nullable = false)
    FormResponseEntity response;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "frq_question_id", nullable = false)
    FormQuestionEntity question;

    @Column(name = "frq_answer", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    List<String> answer;

    @CreatedBy
    @Column(name = "frq_created_by", length = 12, updatable = false)
    String createdBy;

    @CreatedDate
    @Column(name = "frq_created_at", updatable = false)
    Timestamp createdAt;

    @LastModifiedBy
    @Column(name = "frq_updated_by", length = 12, insertable = false)
    String updatedBy;

    @LastModifiedDate
    @Column(name = "frq_updated_at", insertable = false)
    Timestamp updatedAt;

    @PrePersist
    public void prePersist() {
        if (this.id == null || this.id.isEmpty()) {
            this.id = NanoIdUtilsCustom.genNanoIdLimit12();
        }
    }
}
