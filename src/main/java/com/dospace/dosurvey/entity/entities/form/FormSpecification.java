package com.dospace.dosurvey.entity.entities.form;

import com.dospace.dosurvey.entity.enums.DeleteStatus;
import com.dospace.dosurvey.entity.enums.FormFor;
import com.dospace.dosurvey.entity.enums.FormStatus;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class FormSpecification {

    public static Specification<FormEntity> filterBy(
            String userId,
            String tenantId,
            boolean includeSharedForms,
            FormStatus status,
            DeleteStatus deleteStatus,
            FormFor formFor,
            String categoryId
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Base condition: user owns the form OR user is collaborator
            Predicate ownerPredicate = cb.equal(root.get("ownerId"), userId);

            if (includeSharedForms) {
                Join<FormEntity, FormCollaboratorEntity> collaboratorJoin = root.join("collaborators", JoinType.LEFT);
                Predicate collaboratorPredicate = cb.equal(collaboratorJoin.get("accountId"), userId);
                predicates.add(cb.or(ownerPredicate, collaboratorPredicate));
                query.distinct(true);
            } else {
                predicates.add(ownerPredicate);
            }

            // Tenant filter
            if (tenantId != null) {
                predicates.add(cb.equal(root.get("tenantId"), tenantId));
            }

            // Status filter
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            // Delete status filter
            if (deleteStatus != null) {
                predicates.add(cb.equal(root.get("deleteStatus"), deleteStatus));
            } else {
                predicates.add(cb.equal(root.get("deleteStatus"), DeleteStatus.ACTIVE));
            }

            // FormFor filter
            if (formFor != null && formFor != FormFor.ALL) {
                predicates.add(cb.equal(root.get("formFor"), formFor));
            }

            // Category filter
            if (categoryId != null && !categoryId.isEmpty()) {
                predicates.add(cb.equal(root.get("category").get("id"), categoryId));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
