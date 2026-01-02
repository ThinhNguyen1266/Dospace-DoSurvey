package com.dospace.dosurvey.repository.form;

import com.dospace.dosurvey.entity.entities.form.FormResponseEntity;
import com.dospace.dosurvey.entity.enums.DeleteStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FormResponseRepository extends JpaRepository<FormResponseEntity, String> {

    Page<FormResponseEntity> findAllByFormIdAndDeleteStatus(String formId, DeleteStatus deleteStatus, Pageable pageable);

    List<FormResponseEntity> findAllByFormIdAndDeleteStatus(String formId, DeleteStatus deleteStatus);

    Optional<FormResponseEntity> findByIdAndDeleteStatus(String id, DeleteStatus deleteStatus);

    long countByFormIdAndDeleteStatus(String formId, DeleteStatus deleteStatus);

    @Query("SELECT fr FROM FormResponseEntity fr WHERE fr.form.id = :formId AND fr.userId = :userId AND fr.deleteStatus = :deleteStatus")
    Optional<FormResponseEntity> findByFormIdAndUserIdAndDeleteStatus(String formId, String userId, DeleteStatus deleteStatus);

    @Query("SELECT fr FROM FormResponseEntity fr WHERE fr.bookingId = :bookingId AND fr.deleteStatus = :deleteStatus")
    Optional<FormResponseEntity> findByBookingIdAndDeleteStatus(String bookingId, DeleteStatus deleteStatus);

    boolean existsByFormIdAndUserIdAndDeleteStatus(String formId, String userId, DeleteStatus deleteStatus);
}
