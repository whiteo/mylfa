package de.whiteo.mylfa.api;

import de.whiteo.mylfa.dto.user.UserLoginRequest;
import de.whiteo.mylfa.dto.user.UserLoginResponse;
import de.whiteo.mylfa.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/authenticate")
public class AuthenticateRestController {

    private final UserService service;

    @PostMapping
    public ResponseEntity<UserLoginResponse> authenticate(@Valid @RequestBody UserLoginRequest request) {
        log.debug("Start call 'authenticate' with parameters: {}", request);
        UserLoginResponse response = service.getToken(null, request);
        log.debug("End call 'authenticate' with answer {}", response);
        return ResponseEntity.ok(response);
    }

    @GetMapping()
    public ResponseEntity<Boolean> validate(HttpServletRequest request) {
        log.debug("Start call 'validate'");
        Boolean response = service.validateToken(request);
        log.debug("End call 'validate' with answer {}", response);
        return ResponseEntity.ok(response);
    }
}