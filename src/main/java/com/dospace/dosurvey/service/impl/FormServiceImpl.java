package com.dospace.dosurvey.service.impl;

import com.dospace.dosurvey.dto.request.CollaboratorDto;
import com.dospace.dosurvey.dto.request.FormAIRequest;
import com.dospace.dosurvey.dto.request.FormPageRequest;
import com.dospace.dosurvey.dto.request.FormQuestionRequest;
import com.dospace.dosurvey.dto.request.FormRequest;
import com.dospace.dosurvey.dto.request.TransferFormOwnershipRequest;
import com.dospace.dosurvey.dto.request.UpdateSharingRequest;
import com.dospace.dosurvey.dto.response.FormAIResponse;
import com.dospace.dosurvey.dto.response.FormEditorResponse;
import com.dospace.dosurvey.dto.response.FormOptions;
import com.dospace.dosurvey.dto.response.FormResponse;
import com.dospace.dosurvey.dto.response.SharingSettingsResponse;
import com.dospace.dosurvey.entity.FormCategoryEntity;
import com.dospace.dosurvey.entity.FormCollaboratorEntity;
import com.dospace.dosurvey.entity.FormEntity;
import com.dospace.dosurvey.entity.FormPageEntity;
import com.dospace.dosurvey.entity.FormQuestionEntity;
import com.dospace.dosurvey.entity.FormSpecification;
import com.dospace.dosurvey.entity.enums.AccessPermission;
import com.dospace.dosurvey.entity.enums.DeleteStatus;
import com.dospace.dosurvey.entity.enums.FormFor;
import com.dospace.dosurvey.entity.enums.FormStatus;
import com.dospace.dosurvey.exception.AppException;
import com.dospace.dosurvey.exception.ErrorCode;
import com.dospace.dosurvey.mapper.FormMapper;
import com.dospace.dosurvey.repository.FormCategoryRepository;
import com.dospace.dosurvey.repository.FormCollaboratorRepository;
import com.dospace.dosurvey.repository.FormPageRepository;
import com.dospace.dosurvey.repository.FormQuestionRepository;
import com.dospace.dosurvey.repository.FormRepository;
import com.dospace.dosurvey.service.interfaces.FormService;
import com.dospace.dosurvey.context.TenantContextHolder;
import com.dospace.dosurvey.utils.SecurityContextUtil;
import com.dospace.dosurvey.utils.NanoIdUtilsCustom;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FormServiceImpl implements FormService {

    FormRepository formRepository;
    FormCategoryRepository formCategoryRepository;
    FormPageRepository formPageRepository;
    FormQuestionRepository formQuestionRepository;
    FormCollaboratorRepository formCollaboratorRepository;
    FormMapper formMapper;

    @Override
    @Transactional
    public FormResponse create(FormRequest request) {
        String userId = SecurityContextUtil.getCurrentAccountId();
        String tenantId = TenantContextHolder.getCurrentTenant();

        FormEntity formEntity = formMapper.fromRequestToEntity(request);
        formEntity.setOwnerId(userId);
        formEntity.setTenantId(tenantId);
        formEntity.setPublicLink(NanoIdUtilsCustom.genNanoIdLimit16());
        formEntity.setEditorLink(NanoIdUtilsCustom.genNanoIdLimit16());

        // Set category if provided
        if (request.getCategoryId() != null) {
            FormCategoryEntity category = formCategoryRepository
                    .findByIdAndDeleteStatus(request.getCategoryId(), DeleteStatus.ACTIVE)
                    .orElseThrow(() -> new AppException(ErrorCode.FORM_CATEGORY_NOT_FOUND));
            formEntity.setCategory(category);
        }

        formEntity = formRepository.save(formEntity);

        // Process pages and questions
        if (request.getPages() != null && !request.getPages().isEmpty()) {
            processPages(formEntity, request.getPages());
        }

        // Process collaborators
        if (request.getCollaborators() != null && !request.getCollaborators().isEmpty()) {
            processCollaborators(formEntity, request.getCollaborators());
        }

        return formMapper.fromEntityToResponse(formRepository.findById(formEntity.getId()).orElseThrow());
    }

    @Override
    public FormResponse getById(String id) {
        FormEntity entity = formRepository.findByIdAndDeleteStatus(id, DeleteStatus.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.FORM_NOT_FOUND));
        return formMapper.fromEntityToResponse(entity);
    }

    @Override
    public FormEditorResponse getByEditorLink(String link) {
        FormEntity entity = formRepository.findByEditorLink(link)
                .orElseThrow(() -> new AppException(ErrorCode.FORM_NOT_FOUND));

        if (!entity.isActive()) {
            throw new AppException(ErrorCode.FORM_NOT_AVAILABLE);
        }

        return formMapper.fromEntityToEditorResponse(entity);
    }

    @Override
    public FormResponse getByPublicLink(String link) {
        FormEntity entity = formRepository.findByPublicLink(link)
                .orElseThrow(() -> new AppException(ErrorCode.FORM_NOT_FOUND));

        if (!entity.isPublic()) {
            throw new AppException(ErrorCode.FORM_NOT_PUBLIC);
        }

        return formMapper.fromEntityToResponse(entity);
    }

    @Override
    public Page<FormResponse> getAll(FormStatus status, FormFor formFor, String categoryId, boolean includeShared,
            Pageable pageable) {
        String userId = SecurityContextUtil.getCurrentAccountId();
        String tenantId = TenantContextHolder.getCurrentTenant();

        Specification<FormEntity> spec = FormSpecification.filterBy(
                userId,
                tenantId,
                includeShared,
                status,
                DeleteStatus.ACTIVE,
                formFor,
                categoryId);

        return formRepository.findAll(spec, pageable)
                .map(formMapper::fromEntityToResponse);
    }

    @Override
    public List<FormOptions> getAllOptions() {
        String userId = SecurityContextUtil.getCurrentAccountId();
        return formRepository.findAllByOwnerIdAndDeleteStatus(userId, DeleteStatus.ACTIVE)
                .stream()
                .map(formMapper::fromEntityToOption)
                .toList();
    }

    @Override
    @Transactional
    public FormResponse update(String id, FormRequest request) {
        FormEntity entity = formRepository.findByIdAndDeleteStatus(id, DeleteStatus.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.FORM_NOT_FOUND));

        checkFormAccess(entity);

        formMapper.updateFromRequestToEntity(request, entity);

        // Update category
        if (request.getCategoryId() != null) {
            FormCategoryEntity category = formCategoryRepository
                    .findByIdAndDeleteStatus(request.getCategoryId(), DeleteStatus.ACTIVE)
                    .orElseThrow(() -> new AppException(ErrorCode.FORM_CATEGORY_NOT_FOUND));
            entity.setCategory(category);
        } else {
            entity.setCategory(null);
        }

        entity = formRepository.save(entity);

        // Sync pages and questions
        if (request.getPages() != null) {
            syncPages(entity, request.getPages());
        }

        return formMapper.fromEntityToResponse(formRepository.findById(entity.getId()).orElseThrow());
    }

    @Override
    @Transactional
    public void delete(String id) {
        FormEntity entity = formRepository.findByIdAndDeleteStatus(id, DeleteStatus.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.FORM_NOT_FOUND));

        checkFormAccess(entity);

        entity.setDeleteStatus(DeleteStatus.DELETED);
        formRepository.save(entity);
    }

    @Override
    public FormAIResponse getAIResponse(String id) {
        FormEntity entity = formRepository.findByIdAndDeleteStatus(id, DeleteStatus.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.FORM_NOT_FOUND));
        return FormAIResponse.builder()
                .id(entity.getId())
                .aiResponse(entity.getAiResponse())
                .build();
    }

    @Override
    @Transactional
    public FormAIResponse saveAIResponse(String id, FormAIRequest request) {
        FormEntity entity = formRepository.findByIdAndDeleteStatus(id, DeleteStatus.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.FORM_NOT_FOUND));

        checkFormAccess(entity);

        entity.setAiAccept(true);
        entity.setAiResponse(request.getAiResponse());
        entity = formRepository.save(entity);

        return FormAIResponse.builder()
                .id(entity.getId())
                .aiResponse(entity.getAiResponse())
                .build();
    }

    @Override
    public SharingSettingsResponse getSharingSettings(String id) {
        FormEntity entity = formRepository.findByIdAndDeleteStatus(id, DeleteStatus.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.FORM_NOT_FOUND));

        List<FormCollaboratorEntity> collaborators = formCollaboratorRepository.findAllByFormId(id);

        return buildSharingSettingsResponse(entity, collaborators);
    }

    @Override
    @Transactional
    public SharingSettingsResponse updateSharingSettings(String id, UpdateSharingRequest request) {
        FormEntity entity = formRepository.findByIdAndDeleteStatus(id, DeleteStatus.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.FORM_NOT_FOUND));

        checkFormAccess(entity);

        if (request.getEditorAccess() != null) {
            entity.setEditorAccess(request.getEditorAccess());
        }
        if (request.getResponderAccess() != null) {
            entity.setResponderAccess(request.getResponderAccess());
        }
        if (request.getEditorsCanShare() != null) {
            entity.setEditorsCanShare(request.getEditorsCanShare());
        }

        entity = formRepository.save(entity);

        // Sync collaborators
        if (request.getCollaborators() != null) {
            syncCollaborators(entity, request.getCollaborators());
        }

        List<FormCollaboratorEntity> collaborators = formCollaboratorRepository.findAllByFormId(id);
        return buildSharingSettingsResponse(entity, collaborators);
    }

    @Override
    @Transactional
    public FormResponse transferOwnership(String id, TransferFormOwnershipRequest request) {
        FormEntity entity = formRepository.findByIdAndDeleteStatus(id, DeleteStatus.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.FORM_NOT_FOUND));

        String currentUserId = SecurityContextUtil.getCurrentAccountId();
        if (!entity.getOwnerId().equals(currentUserId)) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }

        entity.setOwnerId(request.getNewOwnerId());
        entity = formRepository.save(entity);

        return formMapper.fromEntityToResponse(entity);
    }

    // Helper methods

    private void checkFormAccess(FormEntity entity) {
        String userId = SecurityContextUtil.getCurrentAccountId();
        if (entity.getOwnerId().equals(userId)) {
            return;
        }

        // Check if user is a collaborator with EDITOR role
        formCollaboratorRepository.findByFormIdAndAccountId(entity.getId(), userId)
                .filter(collab -> collab.getRole() == com.dospace.dosurvey.entity.enums.FormRole.EDITOR)
                .orElseThrow(() -> new AppException(ErrorCode.FORBIDDEN));
    }

    private void processPages(FormEntity form, List<FormPageRequest> pageRequests) {
        List<FormPageEntity> pages = new ArrayList<>();
        int pageOrder = 1;

        for (FormPageRequest pageRequest : pageRequests) {
            FormPageEntity pageEntity = formMapper.fromPageRequestToEntity(pageRequest);
            pageEntity.setForm(form);
            pageEntity.setOrder(pageRequest.getOrder() != null ? pageRequest.getOrder() : pageOrder++);
            pageEntity = formPageRepository.save(pageEntity);

            if (pageRequest.getQuestions() != null) {
                processQuestions(pageEntity, pageRequest.getQuestions());
            }

            pages.add(pageEntity);
        }

        form.setPages(pages);
    }

    private void processQuestions(FormPageEntity page, List<FormQuestionRequest> questionRequests) {
        List<FormQuestionEntity> questions = new ArrayList<>();
        int questionOrder = 1;

        for (FormQuestionRequest questionRequest : questionRequests) {
            FormQuestionEntity questionEntity = formMapper.fromQuestionRequestToEntity(questionRequest);
            questionEntity.setPage(page);
            questionEntity.setOrder(questionRequest.getOrder() != null ? questionRequest.getOrder() : questionOrder++);

            if (questionEntity.getAlias() == null || questionEntity.getAlias().isEmpty()) {
                questionEntity.setAlias("q" + questionOrder);
            }

            // Handle grid rows
            if (questionRequest.getQuestionType() == com.dospace.dosurvey.entity.enums.QuestionType.SINGLE_CHOICE_GRID
                    ||
                    questionRequest
                            .getQuestionType() == com.dospace.dosurvey.entity.enums.QuestionType.MULTIPLE_CHOICE_GRID) {
                if (questionRequest.getGridRows() != null) {
                    questionEntity.setGridRows(new ArrayList<>(questionRequest.getGridRows()));
                } else {
                    questionEntity.setGridRows(new ArrayList<>());
                }
            } else {
                questionEntity.setGridRows(null);
            }

            questionEntity = formQuestionRepository.save(questionEntity);
            questions.add(questionEntity);
        }

        page.setQuestions(questions);
    }

    private void processCollaborators(FormEntity form, List<CollaboratorDto> collaboratorDtos) {
        for (CollaboratorDto dto : collaboratorDtos) {
            FormCollaboratorEntity collaborator = FormCollaboratorEntity.builder()
                    .form(form)
                    .email(dto.getEmail())
                    .role(dto.getRole())
                    .build();
            formCollaboratorRepository.save(collaborator);
        }
    }

    private void syncPages(FormEntity form, List<FormPageRequest> pageRequests) {
        List<FormPageEntity> existingPages = formPageRepository.findAllByFormIdOrderByOrderAsc(form.getId());
        Map<String, FormPageEntity> existingPageMap = existingPages.stream()
                .filter(p -> p.getId() != null)
                .collect(Collectors.toMap(FormPageEntity::getId, Function.identity()));

        Set<String> requestPageIds = new HashSet<>();
        int pageOrder = 1;

        for (FormPageRequest pageRequest : pageRequests) {
            FormPageEntity pageEntity;

            if (pageRequest.getId() != null && existingPageMap.containsKey(pageRequest.getId())) {
                // Update existing page
                pageEntity = existingPageMap.get(pageRequest.getId());
                formMapper.updateFromPageRequestToEntity(pageRequest, pageEntity);
                requestPageIds.add(pageRequest.getId());
            } else {
                // Create new page
                pageEntity = formMapper.fromPageRequestToEntity(pageRequest);
                pageEntity.setForm(form);
            }

            pageEntity.setOrder(pageRequest.getOrder() != null ? pageRequest.getOrder() : pageOrder++);
            pageEntity = formPageRepository.save(pageEntity);

            if (pageRequest.getQuestions() != null) {
                syncQuestions(pageEntity, pageRequest.getQuestions());
            }
        }

        // Delete pages not in request
        for (FormPageEntity existingPage : existingPages) {
            if (!requestPageIds.contains(existingPage.getId())) {
                formPageRepository.delete(existingPage);
            }
        }
    }

    private void syncQuestions(FormPageEntity page, List<FormQuestionRequest> questionRequests) {
        List<FormQuestionEntity> existingQuestions = formQuestionRepository
                .findAllByPageIdOrderByOrderAsc(page.getId());
        Map<String, FormQuestionEntity> existingQuestionMap = existingQuestions.stream()
                .filter(q -> q.getId() != null)
                .collect(Collectors.toMap(FormQuestionEntity::getId, Function.identity()));

        Set<String> requestQuestionIds = new HashSet<>();
        int questionOrder = 1;

        for (FormQuestionRequest questionRequest : questionRequests) {
            FormQuestionEntity questionEntity;

            if (questionRequest.getId() != null && existingQuestionMap.containsKey(questionRequest.getId())) {
                // Update existing question
                questionEntity = existingQuestionMap.get(questionRequest.getId());
                formMapper.updateFromQuestionRequestToEntity(questionRequest, questionEntity);
                requestQuestionIds.add(questionRequest.getId());
            } else {
                // Create new question
                questionEntity = formMapper.fromQuestionRequestToEntity(questionRequest);
                questionEntity.setPage(page);
            }

            questionEntity.setOrder(questionRequest.getOrder() != null ? questionRequest.getOrder() : questionOrder++);

            if (questionEntity.getAlias() == null || questionEntity.getAlias().isEmpty()) {
                questionEntity.setAlias("q" + questionOrder);
            }

            // Handle grid rows
            if (questionRequest.getQuestionType() == com.dospace.dosurvey.entity.enums.QuestionType.SINGLE_CHOICE_GRID
                    ||
                    questionRequest
                            .getQuestionType() == com.dospace.dosurvey.entity.enums.QuestionType.MULTIPLE_CHOICE_GRID) {
                if (questionRequest.getGridRows() != null) {
                    questionEntity.setGridRows(new ArrayList<>(questionRequest.getGridRows()));
                } else {
                    questionEntity.setGridRows(new ArrayList<>());
                }
            } else {
                questionEntity.setGridRows(null);
            }

            formQuestionRepository.save(questionEntity);
        }

        // Delete questions not in request
        for (FormQuestionEntity existingQuestion : existingQuestions) {
            if (!requestQuestionIds.contains(existingQuestion.getId())) {
                formQuestionRepository.delete(existingQuestion);
            }
        }
    }

    private void syncCollaborators(FormEntity form, List<CollaboratorDto> collaboratorDtos) {
        formCollaboratorRepository.deleteAllByFormId(form.getId());

        for (CollaboratorDto dto : collaboratorDtos) {
            FormCollaboratorEntity collaborator = FormCollaboratorEntity.builder()
                    .form(form)
                    .email(dto.getEmail())
                    .role(dto.getRole())
                    .build();
            formCollaboratorRepository.save(collaborator);
        }
    }

    private SharingSettingsResponse buildSharingSettingsResponse(FormEntity entity,
            List<FormCollaboratorEntity> collaborators) {
        List<SharingSettingsResponse.CollaboratorResponse> collaboratorResponses = collaborators.stream()
                .map(collab -> SharingSettingsResponse.CollaboratorResponse.builder()
                        .accountId(collab.getAccountId())
                        .email(collab.getEmail())
                        .role(collab.getRole())
                        .build())
                .toList();

        return SharingSettingsResponse.builder()
                .ownerId(entity.getOwnerId())
                .collaborators(collaboratorResponses)
                .editorAccess(buildAccessSettings(entity.getEditorAccess()))
                .responderAccess(buildAccessSettings(entity.getResponderAccess()))
                .editorsCanShare(entity.getEditorsCanShare() != null && entity.getEditorsCanShare())
                .build();
    }

    private SharingSettingsResponse.AccessSettings buildAccessSettings(AccessPermission currentLevel) {
        return SharingSettingsResponse.AccessSettings.builder()
                .currentLevel(currentLevel)
                .availableOptions(Arrays.stream(AccessPermission.values())
                        .map(p -> SharingSettingsResponse.AccessPermissionOption.builder()
                                .value(p)
                                .label(p.name())
                                .description(getAccessPermissionDescription(p))
                                .build())
                        .toList())
                .build();
    }

    private String getAccessPermissionDescription(AccessPermission permission) {
        return switch (permission) {
            case RESTRICTED -> "Only invited users can access";
            case TENANT -> "All users in the tenant can access";
            case ANYONE_WITH_LINK -> "Anyone with the link can access";
        };
    }
}
