package de.whiteo.mylfa.repository;

import de.whiteo.mylfa.builder.CurrencyTypeBuilder;
import de.whiteo.mylfa.builder.UserBuilder;
import de.whiteo.mylfa.domain.CurrencyType;
import de.whiteo.mylfa.domain.User;
import de.whiteo.mylfa.exception.NotFoundObjectException;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.RandomStringUtils;
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
class CurrencyTypeRepositoryTest {

    @Autowired
    private CurrencyTypeRepository repository;
    private CurrencyTypeBuilder currencyTypeBuilder;
    @Autowired
    private UserRepository userRepository;
    private UserBuilder userBuilder;

    @PostConstruct
    void initialize() {
        userBuilder = new UserBuilder(userRepository);
        currencyTypeBuilder = new CurrencyTypeBuilder(repository);
    }

    @Test
    void get() {
        CurrencyType currencyType = prepareTest();
        CurrencyType dbCurrencyType = repository.getByIdAndUserId(currencyType.getId(), currencyType.getUserId());
        asserts(currencyType, dbCurrencyType);
    }

    @Test
    void findByNameIgnoreCase() {
        CurrencyType currencyType = prepareTest();
        CurrencyType dbCurrencyType = repository.findByNameIgnoreCase(currencyType.getName()).orElseThrow(
                () -> new NotFoundObjectException("Object not found"));
        asserts(currencyType, dbCurrencyType);
    }

    private void asserts(CurrencyType currencyType, CurrencyType dbCurrencyType) {
        assertEquals(currencyType.getId(), dbCurrencyType.getId());
        assertEquals(currencyType.isHide(), dbCurrencyType.isHide());
        assertEquals(currencyType.getName(), dbCurrencyType.getName());
        assertEquals(currencyType.getUserId(), dbCurrencyType.getUserId());
    }

    private CurrencyType prepareTest() {
        User user = userBuilder.buildUser();
        return currencyTypeBuilder.buildEntity(RandomStringUtils.randomAlphabetic(20), user.getId());
    }
}