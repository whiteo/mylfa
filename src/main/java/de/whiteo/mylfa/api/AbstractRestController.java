package de.whiteo.mylfa.api;

import de.whiteo.mylfa.domain.AbstractEntity;
import de.whiteo.mylfa.security.Auth;
import de.whiteo.mylfa.security.AuthInterceptor;
import de.whiteo.mylfa.service.AbstractService;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@RestController
@RequiredArgsConstructor
@Auth(AuthInterceptor.class)
public abstract class AbstractRestController<E extends AbstractEntity, D, R> {

    private final AbstractService<E, D, R> service;
    private final AuthInterceptor authInterceptor;

    @GetMapping("/find/{id}")
    public ResponseEntity<D> findById(@PathVariable("id") UUID id) {
        D response = service.findById(authInterceptor.getUserName(), id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<Void> create(@Valid @RequestBody R request) {
        service.create(authInterceptor.getUserName(), request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<D> update(@PathVariable("id") UUID id, @Valid @RequestBody R request) {
        D response = service.update(authInterceptor.getUserName(), id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        service.delete(authInterceptor.getUserName(), id);
        return ResponseEntity.noContent().build();
    }
}