package com.dospace.dosurvey.repository.form;

import com.dospace.dosurvey.entity.entities.form.FormPageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormPageRepository extends JpaRepository<FormPageEntity, String> {

    List<FormPageEntity> findAllByFormIdOrderByOrderAsc(String formId);

    void deleteAllByFormId(String formId);

    int countByFormId(String formId);
}
