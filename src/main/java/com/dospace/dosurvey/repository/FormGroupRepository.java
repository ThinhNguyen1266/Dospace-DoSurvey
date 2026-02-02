package com.dospace.dosurvey.repository;

import com.dospace.dosurvey.entity.FormGroupEntity;
import com.dospace.dosurvey.entity.enums.DeleteStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FormGroupRepository extends JpaRepository<FormGroupEntity, String> {

    Optional<FormGroupEntity> findByIdAndDeleteStatus(String id, DeleteStatus deleteStatus);

    Page<FormGroupEntity> findByOwnerIdAndDeleteStatus(String ownerId, DeleteStatus deleteStatus, Pageable pageable);

    @Query("SELECT g FROM FormGroupEntity g " +
            "JOIN g.members m " +
            "WHERE m.accountId = :accountId " +
            "AND m.status = 'ACCEPTED' " +
            "AND g.deleteStatus = :deleteStatus")
    Page<FormGroupEntity> findGroupsByMemberAccountId(
            @Param("accountId") String accountId,
            @Param("deleteStatus") DeleteStatus deleteStatus,
            Pageable pageable);

    @Query("SELECT DISTINCT g FROM FormGroupEntity g " +
            "LEFT JOIN g.members m " +
            "WHERE (g.ownerId = :accountId OR (m.accountId = :accountId AND m.status = 'ACCEPTED')) " +
            "AND g.deleteStatus = :deleteStatus")
    Page<FormGroupEntity> findAllGroupsByAccountId(
            @Param("accountId") String accountId,
            @Param("deleteStatus") DeleteStatus deleteStatus,
            Pageable pageable);
}
