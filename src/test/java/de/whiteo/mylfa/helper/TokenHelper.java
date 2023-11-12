package de.whiteo.mylfa.helper;

import de.whiteo.mylfa.security.TokenInteract;
import de.whiteo.mylfa.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@RequiredArgsConstructor
public class TokenHelper {

    private final TokenInteract tokenInteract;

    public String getToken(String namePassword) {
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .name(namePassword)
                .password(namePassword.toCharArray())
                .build();
        return "Bearer " + tokenInteract.generateToken(userDetails);
    }
}