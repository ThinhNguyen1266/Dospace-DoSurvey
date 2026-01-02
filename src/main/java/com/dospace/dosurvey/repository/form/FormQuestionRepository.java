package com.dospace.dosurvey.repository.form;

import com.dospace.dosurvey.entity.entities.form.FormQuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormQuestionRepository extends JpaRepository<FormQuestionEntity, String> {

    List<FormQuestionEntity> findAllByPageIdOrderByOrderAsc(String pageId);

    void deleteAllByPageId(String pageId);

    int countByPageId(String pageId);
}
