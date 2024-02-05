package com.thewildoasis.modules.users.dto;

import com.thewildoasis.annotations.FieldsValueMatch;
import com.thewildoasis.annotations.PasswordStrength;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@FieldsValueMatch.List({@FieldsValueMatch(
        field = "newPassword",
        fieldMatch = "confirmationPassword",
        message = "Password and confirm password do not match!"
)})
public class ChangePasswordRequest {
    @PasswordStrength
    @NotBlank(message = "Password should not be blank")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String newPassword;

    @PasswordStrength
    @NotBlank(message = "Confirm password should not be blank")
    @Size(min = 8, message = "Confirm password must be at least 8 characters long")
    private String confirmationPassword;
}
