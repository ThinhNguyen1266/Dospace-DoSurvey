package com.dospace.dosurvey.controller;

import com.dospace.dosurvey.dto.APIResponse;
import com.dospace.dosurvey.dto.request.FormResponseRequest;
import com.dospace.dosurvey.dto.response.FormResponseResponse;
import com.dospace.dosurvey.service.interfaces.FormResponseService;
import com.dospace.dosurvey.utils.PaginationUtil;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/forms/{formId}/responses")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FormResponseController {

    FormResponseService formResponseService;

    @PostMapping
    public APIResponse<FormResponseResponse> submit(
            @PathVariable String formId,
            @Valid @RequestBody FormResponseRequest request
    ) {
        return APIResponse.success(formResponseService.submit(formId, request), "Response submitted successfully");
    }

    @GetMapping("/{id}")
    public APIResponse<FormResponseResponse> getById(
            @PathVariable String formId,
            @PathVariable String id
    ) {
        return APIResponse.success(formResponseService.getById(id));
    }

    @GetMapping
    public APIResponse<Page<FormResponseResponse>> getAllByFormId(
            @PathVariable String formId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PaginationUtil.createPageable(page, size, Sort.by("createdAt").descending());
        return APIResponse.success(formResponseService.getAllByFormId(formId, pageable));
    }

    @GetMapping("/count")
    public APIResponse<Long> countByFormId(@PathVariable String formId) {
        return APIResponse.success(formResponseService.countByFormId(formId));
    }

    @DeleteMapping("/{id}")
    public APIResponse<Void> delete(
            @PathVariable String formId,
            @PathVariable String id
    ) {
        formResponseService.delete(id);
        return APIResponse.success(null, "Response deleted successfully");
    }
}
