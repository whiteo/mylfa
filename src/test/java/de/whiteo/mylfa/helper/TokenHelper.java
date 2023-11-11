package de.whiteo.mylfa.helper;

import de.whiteo.mylfa.security.UserDetailsImpl;
import de.whiteo.mylfa.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@RequiredArgsConstructor
public class TokenHelper {

    private final JwtTokenUtil jwtTokenUtil;

    public String getToken(String namePassword) {
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .name(namePassword)
                .password(namePassword.toCharArray())
                .build();
        return "Bearer " + jwtTokenUtil.generateToken(userDetails);
    }
}