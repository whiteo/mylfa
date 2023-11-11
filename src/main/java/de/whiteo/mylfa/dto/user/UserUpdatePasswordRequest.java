package de.whiteo.mylfa.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@Data
public class UserUpdatePasswordRequest {

    @NotNull(message = "Password cannot be null")
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 5, max = 30, message = "Password must be between 5 and 30 characters")
    private String password;

    @NotNull(message = "Old password cannot be null")
    @NotBlank(message = "Old password cannot be blank")
    @Size(min = 5, max = 30, message = "Old password must be between 5 and 30 characters")
    private String oldPassword;
}