package com.dospace.dosurvey.service.impl;

import com.dospace.dosurvey.context.TenantContextHolder;
import com.dospace.dosurvey.dto.request.FormCategoryRequest;
import com.dospace.dosurvey.dto.response.FormCategoryResponse;
import com.dospace.dosurvey.entity.FormCategoryEntity;
import com.dospace.dosurvey.entity.enums.DeleteStatus;
import com.dospace.dosurvey.entity.enums.NormalStatus;
import com.dospace.dosurvey.exception.AppException;
import com.dospace.dosurvey.exception.ErrorCode;
import com.dospace.dosurvey.mapper.FormCategoryMapper;
import com.dospace.dosurvey.repository.FormCategoryRepository;
import com.dospace.dosurvey.repository.FormRepository;
import com.dospace.dosurvey.service.interfaces.FormCategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FormCategoryServiceImpl implements FormCategoryService {

    FormCategoryRepository formCategoryRepository;
    FormRepository formRepository;
    FormCategoryMapper formCategoryMapper;

    @Override
    @Transactional
    public FormCategoryResponse create(FormCategoryRequest request) {
        FormCategoryEntity entity = formCategoryMapper.fromRequestToEntity(request);
        entity.setTenantId(TenantContextHolder.getCurrentTenant());
        entity = formCategoryRepository.save(entity);
        return formCategoryMapper.fromEntityToResponse(entity);
    }

    @Override
    public FormCategoryResponse getById(String id) {
        FormCategoryEntity entity = formCategoryRepository.findByIdAndDeleteStatus(id, DeleteStatus.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.FORM_CATEGORY_NOT_FOUND));
        return formCategoryMapper.fromEntityToResponse(entity);
    }

    @Override
    public Page<FormCategoryResponse> getAll(Pageable pageable) {
        String tenantId = TenantContextHolder.getCurrentTenant();
        if (tenantId != null) {
            return formCategoryRepository.findAllByTenantIdAndDeleteStatus(tenantId, DeleteStatus.ACTIVE, pageable)
                    .map(formCategoryMapper::fromEntityToResponse);
        }
        return formCategoryRepository.findAllByDeleteStatus(DeleteStatus.ACTIVE, pageable)
                .map(formCategoryMapper::fromEntityToResponse);
    }

    @Override
    public List<FormCategoryResponse> getAllActive() {
        return formCategoryRepository.findAllByDeleteStatusAndStatus(DeleteStatus.ACTIVE, NormalStatus.ACTIVE)
                .stream()
                .map(formCategoryMapper::fromEntityToResponse)
                .toList();
    }

    @Override
    @Transactional
    public FormCategoryResponse update(String id, FormCategoryRequest request) {
        FormCategoryEntity entity = formCategoryRepository.findByIdAndDeleteStatus(id, DeleteStatus.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.FORM_CATEGORY_NOT_FOUND));
        formCategoryMapper.updateFromRequestToEntity(request, entity);
        entity = formCategoryRepository.save(entity);
        return formCategoryMapper.fromEntityToResponse(entity);
    }

    @Override
    @Transactional
    public void delete(String id) {
        FormCategoryEntity entity = formCategoryRepository.findByIdAndDeleteStatus(id, DeleteStatus.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.FORM_CATEGORY_NOT_FOUND));

        // Check if any forms are using this category
        long formsCount = formRepository.countByCategoryIdAndDeleteStatus(id, DeleteStatus.ACTIVE);
        if (formsCount > 0) {
            throw new AppException(ErrorCode.FORM_CATEGORY_STILL_IN_USE);
        }

        entity.setDeleteStatus(DeleteStatus.DELETED);
        formCategoryRepository.save(entity);
    }
}
