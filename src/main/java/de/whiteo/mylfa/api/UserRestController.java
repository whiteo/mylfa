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

@RestController
@RequiredArgsConstructor
@Auth(AuthInterceptor.class)
@RequestMapping("/api/v1/user")
public class UserRestController {

    private final UserService service;

    @GetMapping("/find/{id}")
    public ResponseEntity<UserResponse> find(@PathVariable("id") UUID id) {
        UserResponse response = service.find(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<Void> create(@Valid @RequestBody UserCreateRequest request) {
        service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable("id") UUID id,
                                               @Valid @RequestBody UserUpdateRequest updateRequest) {
        UserResponse response = service.update(id, updateRequest);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/edit-properties/{id}")
    public ResponseEntity<UserResponse> updateProperties(@PathVariable("id") UUID id,
                                               @Valid @RequestBody UserUpdatePropertiesRequest updateRequest) {
        UserResponse response = service.updateProperties(id, updateRequest);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/edit-password/{id}")
    public ResponseEntity<UserResponse> updatePassword(@PathVariable("id") UUID id,
                                                       @Valid @RequestBody UserUpdatePasswordRequest updateRequest) {
        UserResponse response = service.updatePassword(id, updateRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}