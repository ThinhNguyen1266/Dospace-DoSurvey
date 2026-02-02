package com.dospace.dosurvey.controller;

import com.dospace.dosurvey.dto.APIResponse;
import com.dospace.dosurvey.dto.request.FormAIRequest;
import com.dospace.dosurvey.dto.request.FormRequest;
import com.dospace.dosurvey.dto.request.TransferFormOwnershipRequest;
import com.dospace.dosurvey.dto.request.UpdateSharingRequest;
import com.dospace.dosurvey.dto.response.*;
import com.dospace.dosurvey.entity.enums.FormFor;
import com.dospace.dosurvey.entity.enums.FormStatus;
import com.dospace.dosurvey.service.interfaces.FormService;
import com.dospace.dosurvey.utils.PaginationUtil;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/forms")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FormController {

    FormService formService;

    @PostMapping
    public APIResponse<FormResponse> create(@Valid @RequestBody FormRequest request) {
        return APIResponse.success(formService.create(request), "Form created successfully");
    }

    @GetMapping("/{id}")
    public APIResponse<FormResponse> getById(@PathVariable String id) {
        return APIResponse.success(formService.getById(id));
    }

    @GetMapping("/editor/{link}")
    public APIResponse<FormEditorResponse> getByEditorLink(@PathVariable String link) {
        return APIResponse.success(formService.getByEditorLink(link));
    }

    @GetMapping("/public/{link}")
    public APIResponse<FormResponse> getByPublicLink(@PathVariable String link) {
        return APIResponse.success(formService.getByPublicLink(link));
    }

    @GetMapping
    public APIResponse<Page<FormResponse>> getAll(
            @RequestParam(required = false) FormStatus status,
            @RequestParam(required = false) FormFor formFor,
            @RequestParam(required = false) String categoryId,
            @RequestParam(defaultValue = "true") boolean includeShared,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PaginationUtil.createPageable(page, size, Sort.by("createdAt").descending());
        return APIResponse.success(formService.getAll(status, formFor, categoryId, includeShared, pageable));
    }

    @GetMapping("/options")
    public APIResponse<List<FormOptions>> getAllOptions() {
        return APIResponse.success(formService.getAllOptions());
    }

    @PutMapping("/{id}")
    public APIResponse<FormResponse> update(
            @PathVariable String id,
            @Valid @RequestBody FormRequest request
    ) {
        return APIResponse.success(formService.update(id, request), "Form updated successfully");
    }

    @DeleteMapping("/{id}")
    public APIResponse<Void> delete(@PathVariable String id) {
        formService.delete(id);
        return APIResponse.success(null, "Form deleted successfully");
    }

    @GetMapping("/{id}/ai-response")
    public APIResponse<FormAIResponse> getAIResponse(@PathVariable String id) {
        return APIResponse.success(formService.getAIResponse(id));
    }

    @PostMapping("/{id}/ai-response")
    public APIResponse<FormAIResponse> saveAIResponse(
            @PathVariable String id,
            @RequestBody FormAIRequest request
    ) {
        return APIResponse.success(formService.saveAIResponse(id, request), "AI response saved successfully");
    }

    @GetMapping("/{id}/sharing-settings")
    public APIResponse<SharingSettingsResponse> getSharingSettings(@PathVariable String id) {
        return APIResponse.success(formService.getSharingSettings(id));
    }

    @PutMapping("/{id}/sharing-settings")
    public APIResponse<SharingSettingsResponse> updateSharingSettings(
            @PathVariable String id,
            @RequestBody UpdateSharingRequest request
    ) {
        return APIResponse.success(formService.updateSharingSettings(id, request), "Sharing settings updated successfully");
    }

    @PostMapping("/{id}/transfer-ownership")
    public APIResponse<FormResponse> transferOwnership(
            @PathVariable String id,
            @Valid @RequestBody TransferFormOwnershipRequest request
    ) {
        return APIResponse.success(formService.transferOwnership(id, request), "Ownership transferred successfully");
    }
}
