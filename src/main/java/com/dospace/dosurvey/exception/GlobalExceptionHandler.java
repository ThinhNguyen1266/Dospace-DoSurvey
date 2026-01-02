package com.dospace.dosurvey.exception;

import com.dospace.dosurvey.dto.APIResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<APIResponse<String>> handleAppException(AppException ex, WebRequest request) {
        ErrorCode errorCode = ex.getErrorCode();
        APIResponse<String> response = APIResponse.error(
                errorCode.getMessage(),
                errorCode.getCode(),
                null,
                getPath(request),
                getTraceId(request)
        );
        return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<APIResponse<String>> handleAccessDenied(AccessDeniedException ex, WebRequest request) {
        APIResponse<String> response = APIResponse.error(
                "You don't have permission to access this resource",
                HttpStatus.FORBIDDEN.value(),
                ex.getMessage(),
                getPath(request),
                getTraceId(request)
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponse<Map<String, String>>> handleValidationErrors(
            MethodArgumentNotValidException ex, WebRequest request) {
        List<APIResponse.ValidationError> validationErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::mapFieldError)
                .collect(Collectors.toList());

        APIResponse<Map<String, String>> response = APIResponse.validationError(
                "Validation failed",
                validationErrors,
                getPath(request),
                getTraceId(request)
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<APIResponse<Map<String, String>>> handleConstraintViolation(
            ConstraintViolationException ex, WebRequest request) {
        List<APIResponse.ValidationError> validationErrors = ex.getConstraintViolations().stream()
                .map(this::mapConstraintViolation)
                .collect(Collectors.toList());
        APIResponse<Map<String, String>> response = APIResponse.validationError(
                "Validation failed",
                validationErrors,
                getPath(request),
                getTraceId(request)
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<APIResponse<String>> handleIllegalArgument(
            IllegalArgumentException ex, WebRequest request) {
        APIResponse<String> response = APIResponse.error(
                "Invalid Request",
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                getPath(request),
                getTraceId(request)
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<APIResponse<String>> handleRuntimeException(RuntimeException ex, WebRequest request) {
        log.error("Runtime exception", ex);
        APIResponse<String> response = APIResponse.error(
                "Internal server error",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occurred",
                getPath(request),
                getTraceId(request)
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    private String getPath(WebRequest request) {
        return request.getDescription(false).replace("uri=", "");
    }

    private String getTraceId(WebRequest request) {
        String traceId = request.getHeader("X-Trace-ID");
        if (traceId == null) {
            traceId = request.getHeader("X-B3-TraceId");
        }
        if (traceId == null) {
            traceId = java.util.UUID.randomUUID().toString();
        }
        return traceId;
    }

    private APIResponse.ValidationError mapFieldError(FieldError fieldError) {
        return new APIResponse.ValidationError(fieldError.getField(), fieldError.getDefaultMessage());
    }

    private APIResponse.ValidationError mapConstraintViolation(ConstraintViolation<?> violation) {
        return new APIResponse.ValidationError(
                violation.getPropertyPath().toString(),
                violation.getMessage()
        );
    }
}
