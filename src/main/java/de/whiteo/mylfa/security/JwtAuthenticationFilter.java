package de.whiteo.mylfa.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.whiteo.mylfa.dto.user.UserLoginRequest;
import de.whiteo.mylfa.exception.AppRuntimeException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            UserLoginRequest loginRequest = new ObjectMapper().readValue(request.getInputStream(), UserLoginRequest.class);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(), loginRequest.getPassword());
            return getAuthenticationManager().authenticate(authToken);
        } catch (IOException e) {
            throw new AppRuntimeException(e);
        }
    }
}