package com.dospace.dosurvey.repository;

import com.dospace.dosurvey.entity.FormEntity;
import com.dospace.dosurvey.entity.enums.DeleteStatus;
import com.dospace.dosurvey.entity.enums.FormFor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FormRepository extends JpaRepository<FormEntity, String>, JpaSpecificationExecutor<FormEntity> {

    Optional<FormEntity> findByIdAndDeleteStatus(String id, DeleteStatus deleteStatus);

    Optional<FormEntity> findByPublicLink(String publicLink);

    Optional<FormEntity> findByEditorLink(String editorLink);

    Page<FormEntity> findAllByOwnerIdAndDeleteStatus(String ownerId, DeleteStatus deleteStatus, Pageable pageable);

    List<FormEntity> findAllByOwnerIdAndDeleteStatus(String ownerId, DeleteStatus deleteStatus);

    @Query("SELECT f FROM FormEntity f WHERE f.category.id = :categoryId AND f.deleteStatus = :deleteStatus")
    List<FormEntity> findAllByCategoryIdAndDeleteStatus(String categoryId, DeleteStatus deleteStatus);

    @Query("SELECT COUNT(f) FROM FormEntity f WHERE f.category.id = :categoryId AND f.deleteStatus = :deleteStatus")
    long countByCategoryIdAndDeleteStatus(String categoryId, DeleteStatus deleteStatus);

    List<FormEntity> findAllByFormForAndDeleteStatus(FormFor formFor, DeleteStatus deleteStatus);

    boolean existsByPublicLink(String publicLink);

    boolean existsByEditorLink(String editorLink);
}
