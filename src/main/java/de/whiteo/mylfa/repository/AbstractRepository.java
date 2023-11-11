package de.whiteo.mylfa.repository;

import de.whiteo.mylfa.domain.AbstractEntity;
import de.whiteo.mylfa.exception.NotFoundObjectException;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@Primary
public interface AbstractRepository<E extends AbstractEntity> extends JpaRepository<E, UUID> {

    default E getByIdAndUserId(UUID id, UUID userId) {
        if (null == id) {
            throw new NotFoundObjectException("ID is blank");
        }
        if (null == userId) {
            throw new NotFoundObjectException("User ID is blank");
        }
        return findByIdAndUserId(id, userId).orElseThrow(() -> new NotFoundObjectException(String.format(
                "Object ID = %s not found", id)));
    }

    default E getOrThrow(UUID id) {
        if (null == id) {
            throw new NotFoundObjectException("ID is blank");
        }
        return findById(id).orElseThrow(() -> new NotFoundObjectException(String.format(
                "Object ID = %s not found", id)));
    }

    default E getOrNull(UUID id) {
        if (null == id) {
            return null;
        }
        return findById(id).orElseThrow(() -> new NotFoundObjectException(String.format(
                "Object ID = %s not found", id)));
    }

    Optional<E> findByIdAndUserId(UUID id, UUID userId);

    void deleteAllByUserId(UUID userId);
}