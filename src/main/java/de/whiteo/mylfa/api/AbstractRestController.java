package de.whiteo.mylfa.api;

import de.whiteo.mylfa.domain.AbstractEntity;
import de.whiteo.mylfa.security.Auth;
import de.whiteo.mylfa.security.AuthInterceptor;
import de.whiteo.mylfa.service.AbstractService;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@Slf4j
@RestController
@RequiredArgsConstructor
@Auth(AuthInterceptor.class)
public abstract class AbstractRestController<E extends AbstractEntity, D, R> {

    private final AbstractService<E, D, R> service;
    private final AuthInterceptor authInterceptor;

    @GetMapping("/{id}")
    public ResponseEntity<D> findById(@PathVariable("id") UUID id) {
        log.debug("Start call 'findById' with parameters: {}", id);
        D response = service.findById(authInterceptor.getUserName(), id);
        log.debug("End call 'findById' with answer {}", response);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody R request) {
        log.debug("Start call 'create' with parameters: {}", request);
        service.create(authInterceptor.getUserName(), request);
        log.debug("End call 'create'");
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}/edit")
    public ResponseEntity<D> update(@PathVariable("id") UUID id, @Valid @RequestBody R request) {
        log.debug("Start call 'update' with parameters: {}, {}", id, request);
        D response = service.update(authInterceptor.getUserName(), id, request);
        log.debug("End call 'update' with answer {}", response);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        log.debug("Start call 'delete' with parameters: {}", id);
        service.delete(authInterceptor.getUserName(), id);
        log.debug("End call 'delete'");
        return ResponseEntity.noContent().build();
    }
}