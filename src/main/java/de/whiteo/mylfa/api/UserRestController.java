package de.whiteo.mylfa.api;

import de.whiteo.mylfa.dto.user.UserCreateRequest;
import de.whiteo.mylfa.dto.user.UserResponse;
import de.whiteo.mylfa.dto.user.UserUpdatePasswordRequest;
import de.whiteo.mylfa.dto.user.UserUpdatePropertiesRequest;
import de.whiteo.mylfa.dto.user.UserUpdateRequest;
import de.whiteo.mylfa.security.Auth;
import de.whiteo.mylfa.security.AuthInterceptor;
import de.whiteo.mylfa.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@Auth(AuthInterceptor.class)
public class UserRestController {

    private final UserService service;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> find(@PathVariable("id") UUID id) {
        log.debug("Start call 'find' with parameters: {}", id);
        UserResponse response = service.find(id);
        log.debug("End call 'find' with answer {}", response);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<Void> create(@Valid @RequestBody UserCreateRequest request) {
        log.debug("Start call 'create' with parameters: {}", request);
        service.create(request);
        log.debug("End call 'create'");
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}/edit")
    public ResponseEntity<UserResponse> update(@PathVariable("id") UUID id,
                                               @Valid @RequestBody UserUpdateRequest updateRequest) {
        log.debug("Start call 'update' with parameters: {} {}", id, updateRequest);
        UserResponse response = service.update(id, updateRequest);
        log.debug("End call 'update' with answer {}", response);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/edit-properties")
    public ResponseEntity<UserResponse> updateProperties(@PathVariable("id") UUID id,
                                               @Valid @RequestBody UserUpdatePropertiesRequest updateRequest) {
        log.debug("Start call 'updateProperties' with parameters: {} {}", id, updateRequest);
        UserResponse response = service.updateProperties(id, updateRequest);
        log.debug("End call 'updateProperties' with answer {}", response);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/edit-password")
    public ResponseEntity<UserResponse> updatePassword(@PathVariable("id") UUID id,
                                                       @Valid @RequestBody UserUpdatePasswordRequest updateRequest) {
        log.debug("Start call 'updatePassword' with parameters: {} {}", id, updateRequest);
        UserResponse response = service.updatePassword(id, updateRequest);
        log.debug("End call 'updatePassword' with answer {}", response);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        log.debug("Start call 'delete' with parameters: {}", id);
        service.delete(id);
        log.debug("End call 'delete'");
        return ResponseEntity.noContent().build();
    }
}