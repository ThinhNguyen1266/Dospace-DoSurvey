package com.dospace.dosurvey.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIResponse<T> {

    @JsonProperty("success")
    boolean success;

    @JsonProperty("code")
    int code;

    @JsonProperty("message")
    String message;

    @JsonProperty("data")
    T data;

    @JsonProperty("timestamp")
    Instant timestamp;

    @JsonProperty("path")
    String path;

    @JsonProperty("traceId")
    String traceId;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ValidationError {
        @JsonProperty("field")
        String field;

        @JsonProperty("message")
        String message;
    }

    public static <T> APIResponse<T> success(T data) {
        return APIResponse.<T>builder()
                .code(200)
                .success(true)
                .message("success")
                .data(data)
                .timestamp(vietnamTime())
                .build();
    }

    public static <T> APIResponse<T> success(T data, String message) {
        return APIResponse.<T>builder()
                .code(200)
                .success(true)
                .message(message)
                .data(data)
                .timestamp(Instant.now())
                .build();
    }

    public static <T> APIResponse<T> success(T data, String message, String path, String traceId) {
        return APIResponse.<T>builder()
                .code(200)
                .success(true)
                .message(message)
                .data(data)
                .timestamp(Instant.now())
                .path(path)
                .traceId(traceId)
                .build();
    }

    // Error response methods
    public static APIResponse<String> error(String message, int code) {
        return APIResponse.<String>builder()
                .code(code)
                .success(false)
                .message(message)
                .timestamp(Instant.now())
                .build();
    }

    public static APIResponse<String> error(String message, int code, String details) {
        return APIResponse.<String>builder()
                .code(code)
                .success(false)
                .message(message)
                .data(details)
                .timestamp(Instant.now())
                .build();
    }

    public static APIResponse<String> error(String message, int code, String details, String path, String traceId) {
        return APIResponse.<String>builder()
                .code(code)
                .success(false)
                .message(message)
                .data(details)
                .timestamp(Instant.now())
                .path(path)
                .traceId(traceId)
                .build();
    }

    // Validation error methods
    public static APIResponse<Map<String, String>> validationError(
            String message,
            List<ValidationError> validationErrors
    ) {
        return APIResponse.<Map<String, String>>builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .success(false)
                .message(message)
                .data(validationErrors.parallelStream()
                        .collect(Collectors.toMap(ValidationError::getField, ValidationError::getMessage)))
                .timestamp(Instant.now())
                .build();
    }

    public static APIResponse<Map<String, String>> validationError(
            String message,
            List<ValidationError> validationErrors,
            String path,
            String traceId) {
        return APIResponse.<Map<String, String>>builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .success(false)
                .message(message)
                .data(validationErrors.parallelStream()
                        .collect(Collectors.toMap(ValidationError::getField, ValidationError::getMessage)))
                .timestamp(Instant.now())
                .path(path)
                .traceId(traceId)
                .build();
    }

    private static Instant vietnamTime() {
        return ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant();
    }
}
