package de.whiteo.mylfa.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@Data
public class UserValidateRequest {

    @NotNull(message = "userId cannot be null")
    private UUID userId;
}