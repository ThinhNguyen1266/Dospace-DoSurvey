package com.dospace.dosurvey.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorCode {
    // Authentication Errors
    UNAUTHENTICATED(401, "Authentication is required.", HttpStatus.UNAUTHORIZED),
    FORBIDDEN(403, "Access is denied.", HttpStatus.FORBIDDEN),
    UNAUTHORIZED(403, "You do not have permission to perform this action.", HttpStatus.FORBIDDEN),

    // User Errors
    USER_NOT_FOUND(404, "User not found.", HttpStatus.NOT_FOUND),

    // Form Category Errors
    FORM_CATEGORY_NOT_FOUND(404, "Form category not found, please check the ID.", HttpStatus.NOT_FOUND),
    FORM_CATEGORY_STILL_IN_USE(400, "The form category is still in use by non-deleted forms.", HttpStatus.BAD_REQUEST),

    // Form Errors
    FORM_NOT_FOUND(404, "Form not found, please check the ID.", HttpStatus.NOT_FOUND),
    FORM_PAGE_NOT_FOUND(404, "Form page not found, please check the ID.", HttpStatus.NOT_FOUND),
    FORM_QUESTION_NOT_FOUND(404, "Form question not found, please check the ID.", HttpStatus.NOT_FOUND),
    FORM_RESPONSE_NOT_FOUND(404, "Form response not found, please check the ID.", HttpStatus.NOT_FOUND),
    FORM_RESPONSE_QUESTION_NOT_FOUND(404, "Question not found in the response, please check the ID.", HttpStatus.NOT_FOUND),
    FORM_PAGE_REQUIRED(400, "The form must have at least one page.", HttpStatus.BAD_REQUEST),
    FORM_QUESTION_REQUIRED(400, "The form page must have at least one question.", HttpStatus.BAD_REQUEST),
    FORM_RESPONSE_ANSWER_REQUIRED(400, "The form response must have an answer.", HttpStatus.BAD_REQUEST),
    FORM_NOT_AVAILABLE(400, "The form is not available.", HttpStatus.BAD_REQUEST),
    FORM_NOT_PUBLIC(400, "The form is not public yet.", HttpStatus.BAD_REQUEST),
    INVALID_TENANT_SETTING(400, "Cannot update tenant access for a form that does not belong to the tenant.", HttpStatus.BAD_REQUEST),
    INVALID_NAVIGATION_TARGET(400, "Invalid navigation target. Must be 'NEXT', 'SUBMIT', or valid pageId.", HttpStatus.BAD_REQUEST),

    // Group Errors
    GROUP_NOT_FOUND(404, "Group not found.", HttpStatus.NOT_FOUND),
    GROUP_ACCESS_DENIED(403, "You do not have access to this group.", HttpStatus.FORBIDDEN),
    ALREADY_GROUP_MEMBER(400, "User is already a member of this group.", HttpStatus.BAD_REQUEST),
    NOT_GROUP_MEMBER(400, "You are not a member of this group.", HttpStatus.BAD_REQUEST),
    CANNOT_REMOVE_OWNER(400, "Cannot remove the group owner.", HttpStatus.BAD_REQUEST),
    INVITATION_NOT_FOUND(404, "Invitation not found.", HttpStatus.NOT_FOUND),
    CANNOT_CHANGE_OWNER_ROLE(400, "Cannot change the owner's role.", HttpStatus.BAD_REQUEST),

    // General Errors
    UNCATEGORIZED_EXCEPTION(500, "An error has occurred", HttpStatus.INTERNAL_SERVER_ERROR);

    int code;
    String message;
    HttpStatus httpStatus;
}
