package com.dospace.dosurvey.controller;

import com.dospace.dosurvey.dto.APIResponse;
import com.dospace.dosurvey.dto.request.FormCategoryRequest;
import com.dospace.dosurvey.dto.response.FormCategoryResponse;
import com.dospace.dosurvey.service.interfaces.FormCategoryService;
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
@RequestMapping("/api/form-categories")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FormCategoryController {

    FormCategoryService formCategoryService;

    @PostMapping
    public APIResponse<FormCategoryResponse> create(@Valid @RequestBody FormCategoryRequest request) {
        return APIResponse.success(formCategoryService.create(request), "Form category created successfully");
    }

    @GetMapping("/{id}")
    public APIResponse<FormCategoryResponse> getById(@PathVariable String id) {
        return APIResponse.success(formCategoryService.getById(id));
    }

    @GetMapping
    public APIResponse<Page<FormCategoryResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PaginationUtil.createPageable(page, size, Sort.by("createdAt").descending());
        return APIResponse.success(formCategoryService.getAll(pageable));
    }

    @GetMapping("/options")
    public APIResponse<List<FormCategoryResponse>> getAllActive() {
        return APIResponse.success(formCategoryService.getAllActive());
    }

    @PutMapping("/{id}")
    public APIResponse<FormCategoryResponse> update(
            @PathVariable String id,
            @Valid @RequestBody FormCategoryRequest request
    ) {
        return APIResponse.success(formCategoryService.update(id, request), "Form category updated successfully");
    }

    @DeleteMapping("/{id}")
    public APIResponse<Void> delete(@PathVariable String id) {
        formCategoryService.delete(id);
        return APIResponse.success(null, "Form category deleted successfully");
    }
}
