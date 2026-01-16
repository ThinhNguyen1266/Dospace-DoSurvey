package com.dospace.dosurvey.dto.request;

import com.dospace.dosurvey.entity.enums.FormRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CollaboratorDto {
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    String email;

    @NotNull(message = "Role is required")
    FormRole role;
}
