package com.dospace.dosurvey.repository;

import com.dospace.dosurvey.entity.FormCategoryEntity;
import com.dospace.dosurvey.entity.enums.DeleteStatus;
import com.dospace.dosurvey.entity.enums.NormalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FormCategoryRepository extends JpaRepository<FormCategoryEntity, String> {

    Page<FormCategoryEntity> findAllByDeleteStatus(DeleteStatus deleteStatus, Pageable pageable);

    List<FormCategoryEntity> findAllByDeleteStatusAndStatus(DeleteStatus deleteStatus, NormalStatus status);

    Optional<FormCategoryEntity> findByIdAndDeleteStatus(String id, DeleteStatus deleteStatus);

    @Query("SELECT fc FROM FormCategoryEntity fc WHERE fc.tenantId = :tenantId AND fc.deleteStatus = :deleteStatus")
    Page<FormCategoryEntity> findAllByTenantIdAndDeleteStatus(String tenantId, DeleteStatus deleteStatus, Pageable pageable);

    boolean existsByNameAndDeleteStatus(String name, DeleteStatus deleteStatus);
}
