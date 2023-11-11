package de.whiteo.mylfa.dto.user;

import jakarta.validation.constraints.Email;
import lombok.Data;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@Data
public class UserUpdateRequest {

    @Email(message = "Email should be valid", regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]" +
            "+(\\.[A-Za-z0-9_-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")
    private String email;
}