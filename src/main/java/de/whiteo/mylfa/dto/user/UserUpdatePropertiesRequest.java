package de.whiteo.mylfa.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@Data
public class UserUpdatePropertiesRequest {

    @NotNull(message = "Properties cannot be null")
    private Map<String, String> properties;
}