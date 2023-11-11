package de.whiteo.mylfa.service;

import de.whiteo.mylfa.config.NoModifyDemoMode;
import de.whiteo.mylfa.domain.AbstractEntity;
import de.whiteo.mylfa.domain.User;
import de.whiteo.mylfa.exception.ExecutionConflictException;
import de.whiteo.mylfa.mapper.AbstractMapper;
import de.whiteo.mylfa.repository.AbstractRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@Service
@RequiredArgsConstructor
public abstract class AbstractService<E extends AbstractEntity, D, R> {

    private final AbstractRepository<E> repository;
    private final AbstractMapper<E, D> mapper;
    private final UserService userService;

    public D findById(String userName, UUID id) {
        User user = userService.findByEmail(userName);
        return mapper.toResponse(repository.getByIdAndUserId(id, user.getId()));
    }

    @NoModifyDemoMode
    public void delete(String userName, UUID id) {
        User user = userService.findByEmail(userName);
        repository.delete(repository.getByIdAndUserId(id, user.getId()));
    }

    public abstract D create(String userName, R request);

    public abstract D update(String userName, UUID id, R request);

    protected void checkCurrencyTypeIds(UUID id1, UUID id2) {
        if (!id1.equals(id2)) {
            throw new ExecutionConflictException("Currency type IDs are different");
        }
    }
}