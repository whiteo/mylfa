package de.whiteo.mylfa.api;

import de.whiteo.mylfa.dto.user.UserLoginRequest;
import de.whiteo.mylfa.dto.user.UserLoginResponse;
import de.whiteo.mylfa.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/authenticate")
public class AuthenticateRestController {

    private final UserService service;

    @PostMapping
    public ResponseEntity<UserLoginResponse> authenticate(@Valid @RequestBody UserLoginRequest request) {
        UserLoginResponse response = service.getToken(null, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping()
    public ResponseEntity<Boolean> validate(HttpServletRequest request) {
        Boolean response = service.validateToken(request);
        return ResponseEntity.ok(response);
    }
}