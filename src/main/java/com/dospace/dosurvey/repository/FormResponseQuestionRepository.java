package com.dospace.dosurvey.repository;

import com.dospace.dosurvey.entity.FormResponseQuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FormResponseQuestionRepository extends JpaRepository<FormResponseQuestionEntity, String> {

    List<FormResponseQuestionEntity> findAllByResponseId(String responseId);

    Optional<FormResponseQuestionEntity> findByResponseIdAndQuestionId(String responseId, String questionId);

    void deleteAllByResponseId(String responseId);

    @Query("SELECT frq FROM FormResponseQuestionEntity frq WHERE frq.question.id = :questionId")
    List<FormResponseQuestionEntity> findAllByQuestionId(String questionId);

    @Query("SELECT frq FROM FormResponseQuestionEntity frq WHERE frq.question.id = :questionId AND frq.answer = :answer")
    List<FormResponseQuestionEntity> findAllByQuestionIdAndAnswer(String questionId, List<String> answer);
}
