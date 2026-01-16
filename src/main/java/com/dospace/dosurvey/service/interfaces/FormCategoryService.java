package com.dospace.dosurvey.service.interfaces;

import com.dospace.dosurvey.dto.request.FormCategoryRequest;
import com.dospace.dosurvey.dto.response.FormCategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FormCategoryService {

    FormCategoryResponse create(FormCategoryRequest request);

    FormCategoryResponse getById(String id);

    Page<FormCategoryResponse> getAll(Pageable pageable);

    List<FormCategoryResponse> getAllActive();

    FormCategoryResponse update(String id, FormCategoryRequest request);

    void delete(String id);
}
