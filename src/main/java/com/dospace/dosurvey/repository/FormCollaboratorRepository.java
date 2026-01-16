package com.dospace.dosurvey.repository;

import com.dospace.dosurvey.entity.FormCollaboratorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FormCollaboratorRepository extends JpaRepository<FormCollaboratorEntity, String> {

    List<FormCollaboratorEntity> findAllByFormId(String formId);

    Optional<FormCollaboratorEntity> findByFormIdAndEmail(String formId, String email);

    Optional<FormCollaboratorEntity> findByFormIdAndAccountId(String formId, String accountId);

    void deleteAllByFormId(String formId);

    boolean existsByFormIdAndAccountId(String formId, String accountId);

    boolean existsByFormIdAndEmail(String formId, String email);
}
