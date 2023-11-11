package de.whiteo.mylfa.dto.user;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@Data
@Builder
public class UserResponse {

    private UUID id;
    private String email;
    private boolean verified;
}