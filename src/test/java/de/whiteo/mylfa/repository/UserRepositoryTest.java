package de.whiteo.mylfa.repository;

import de.whiteo.mylfa.builder.UserBuilder;
import de.whiteo.mylfa.domain.User;
import de.whiteo.mylfa.exception.NotFoundObjectException;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository repository;
    private UserBuilder userBuilder;

    @PostConstruct
    void initialize() {
        userBuilder = new UserBuilder(repository);
    }

    @Test
    void getOrThrow() {
        User user = userBuilder.buildUser();
        User dbUser = repository.getOrThrow(user.getId());
        asserts(user, dbUser);
    }

    @Test
    void findByNameIgnoreCase() {
        User user = userBuilder.buildUser();
        User dbUser = repository.findByEmailIgnoreCase(user.getEmail()).orElseThrow(
                () -> new NotFoundObjectException("Object not found"));
        asserts(user, dbUser);
    }

    private void asserts(User user, User dbUser) {
        assertEquals(user.getId(), dbUser.getId());
        assertEquals(user.getPassword(), dbUser.getPassword());
        assertEquals(user.isVerified(), dbUser.isVerified());
        assertEquals(user.getEmail(), dbUser.getEmail());
    }
}