package com.dospace.dosurvey.service.impl;

import com.dospace.dosurvey.dto.request.FormResponseQuestionRequest;
import com.dospace.dosurvey.dto.request.FormResponseRequest;
import com.dospace.dosurvey.dto.response.FormResponseResponse;
import com.dospace.dosurvey.entity.FormEntity;
import com.dospace.dosurvey.entity.FormQuestionEntity;
import com.dospace.dosurvey.entity.FormResponseEntity;
import com.dospace.dosurvey.entity.FormResponseQuestionEntity;
import com.dospace.dosurvey.entity.enums.DeleteStatus;
import com.dospace.dosurvey.exception.AppException;
import com.dospace.dosurvey.exception.ErrorCode;
import com.dospace.dosurvey.mapper.FormResponseMapper;
import com.dospace.dosurvey.repository.FormQuestionRepository;
import com.dospace.dosurvey.repository.FormRepository;
import com.dospace.dosurvey.repository.FormResponseQuestionRepository;
import com.dospace.dosurvey.repository.FormResponseRepository;
import com.dospace.dosurvey.service.interfaces.FormResponseService;
import com.dospace.dosurvey.utils.SecurityContextUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FormResponseServiceImpl implements FormResponseService {

    FormRepository formRepository;
    FormResponseRepository formResponseRepository;
    FormResponseQuestionRepository formResponseQuestionRepository;
    FormQuestionRepository formQuestionRepository;
    FormResponseMapper formResponseMapper;

    @Override
    @Transactional
    public FormResponseResponse submit(String formId, FormResponseRequest request) {
        FormEntity form = formRepository.findByIdAndDeleteStatus(formId, DeleteStatus.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.FORM_NOT_FOUND));

        if (!form.isPublic()) {
            throw new AppException(ErrorCode.FORM_NOT_AVAILABLE);
        }

        String userId = SecurityContextUtil.getCurrentAccountId();

        FormResponseEntity responseEntity = FormResponseEntity.builder()
                .form(form)
                .userId(userId != null ? userId : request.getUserId())
                .bookingId(request.getBookingId())
                .build();

        responseEntity = formResponseRepository.save(responseEntity);

        // Process answers
        if (request.getAnswers() != null && !request.getAnswers().isEmpty()) {
            List<FormResponseQuestionEntity> answers = new ArrayList<>();

            for (FormResponseQuestionRequest answerRequest : request.getAnswers()) {
                FormQuestionEntity question = formQuestionRepository.findById(answerRequest.getQuestionId())
                        .orElseThrow(() -> new AppException(ErrorCode.FORM_QUESTION_NOT_FOUND));

                FormResponseQuestionEntity answerEntity = FormResponseQuestionEntity.builder()
                        .response(responseEntity)
                        .question(question)
                        .answer(answerRequest.getAnswer())
                        .build();

                answerEntity = formResponseQuestionRepository.save(answerEntity);
                answers.add(answerEntity);
            }

            responseEntity.setAnswers(answers);
        }

        return formResponseMapper.fromEntityToResponse(responseEntity);
    }

    @Override
    public FormResponseResponse getById(String id) {
        FormResponseEntity entity = formResponseRepository.findByIdAndDeleteStatus(id, DeleteStatus.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.FORM_RESPONSE_NOT_FOUND));
        return formResponseMapper.fromEntityToResponse(entity);
    }

    @Override
    public Page<FormResponseResponse> getAllByFormId(String formId, Pageable pageable) {
        return formResponseRepository.findAllByFormIdAndDeleteStatus(formId, DeleteStatus.ACTIVE, pageable)
                .map(formResponseMapper::fromEntityToResponse);
    }

    @Override
    public long countByFormId(String formId) {
        return formResponseRepository.countByFormIdAndDeleteStatus(formId, DeleteStatus.ACTIVE);
    }

    @Override
    @Transactional
    public void delete(String id) {
        FormResponseEntity entity = formResponseRepository.findByIdAndDeleteStatus(id, DeleteStatus.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.FORM_RESPONSE_NOT_FOUND));
        entity.setDeleteStatus(DeleteStatus.DELETED);
        formResponseRepository.save(entity);
    }
}
