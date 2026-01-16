package com.dospace.dosurvey.service.interfaces;

import com.dospace.dosurvey.dto.request.FormResponseRequest;
import com.dospace.dosurvey.dto.response.FormResponseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FormResponseService {

    FormResponseResponse submit(String formId, FormResponseRequest request);

    FormResponseResponse getById(String id);

    Page<FormResponseResponse> getAllByFormId(String formId, Pageable pageable);

    long countByFormId(String formId);

    void delete(String id);
}
