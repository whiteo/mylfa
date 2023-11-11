package de.whiteo.mylfa.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@Data
@AllArgsConstructor
public class UserLoginResponse {

    private String token;
    private UserResponse user;
}