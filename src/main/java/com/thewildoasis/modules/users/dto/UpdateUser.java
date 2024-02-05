package com.thewildoasis.modules.users.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUser {
    @NotBlank(message = "Full name must not be blank.")
    @Size(min = 3, message = "Full name must be at least 3 characters long.")
    private String fullName;

    private String avatar;
}
