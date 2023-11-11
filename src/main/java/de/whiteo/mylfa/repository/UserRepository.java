package de.whiteo.mylfa.repository;

import de.whiteo.mylfa.domain.User;
import de.whiteo.mylfa.exception.NotFoundObjectException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

public interface UserRepository extends JpaRepository<User, UUID> {

    default User getOrThrow(UUID id) {
        if (null == id) {
            throw new NotFoundObjectException("ID is blank");
        }
        return findById(id).orElseThrow(() -> new NotFoundObjectException(String.format(
                "Object ID = %s not found", id)));
    }

    Optional<User> findByEmailIgnoreCase(String email);
}