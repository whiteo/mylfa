package de.whiteo.mylfa.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Map;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@Data
public class UserCreateRequest {

    @Email(message = "Email should be valid", regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]" +
            "+(\\.[A-Za-z0-9_-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")
    private String email;

    @NotNull(message = "Password cannot be null")
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 5, max = 30, message = "Password must be between 5 and 30 characters")
    private String password;

    @NotNull(message = "Properties cannot be null")
    private Map<String, String> properties;
}