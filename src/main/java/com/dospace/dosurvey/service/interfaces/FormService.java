package com.dospace.dosurvey.service.interfaces;

import com.dospace.dosurvey.dto.request.form.FormAIRequest;
import com.dospace.dosurvey.dto.request.form.FormRequest;
import com.dospace.dosurvey.dto.request.form.TransferFormOwnershipRequest;
import com.dospace.dosurvey.dto.request.form.UpdateSharingRequest;
import com.dospace.dosurvey.dto.response.form.FormAIResponse;
import com.dospace.dosurvey.dto.response.form.FormEditorResponse;
import com.dospace.dosurvey.dto.response.form.FormOptions;
import com.dospace.dosurvey.dto.response.form.FormResponse;
import com.dospace.dosurvey.dto.response.form.SharingSettingsResponse;
import com.dospace.dosurvey.entity.enums.FormFor;
import com.dospace.dosurvey.entity.enums.FormStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FormService {

    FormResponse create(FormRequest request);

    FormResponse getById(String id);

    FormEditorResponse getByEditorLink(String link);

    FormResponse getByPublicLink(String link);

    Page<FormResponse> getAll(FormStatus status, FormFor formFor, String categoryId, boolean includeShared, Pageable pageable);

    List<FormOptions> getAllOptions();

    FormResponse update(String id, FormRequest request);

    void delete(String id);

    FormAIResponse getAIResponse(String id);

    FormAIResponse saveAIResponse(String id, FormAIRequest request);

    SharingSettingsResponse getSharingSettings(String id);

    SharingSettingsResponse updateSharingSettings(String id, UpdateSharingRequest request);

    FormResponse transferOwnership(String id, TransferFormOwnershipRequest request);
}
