package de.whiteo.mylfa.repository;

import de.whiteo.mylfa.builder.IncomeCategoryBuilder;
import de.whiteo.mylfa.builder.UserBuilder;
import de.whiteo.mylfa.domain.IncomeCategory;
import de.whiteo.mylfa.domain.User;
import de.whiteo.mylfa.exception.NotFoundObjectException;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class IncomeCategoryRepositoryTest {

    private IncomeCategoryBuilder incomeCategoryBuilder;
    @Autowired
    private IncomeCategoryRepository repository;
    @Autowired
    private UserRepository userRepository;
    private UserBuilder userBuilder;

    @PostConstruct
    void initialize() {
        incomeCategoryBuilder = new IncomeCategoryBuilder(repository);
        userBuilder = new UserBuilder(userRepository);
    }

    @Test
    void getOrThrow() {
        IncomeCategory parentCategory = prepareTest(null);
        IncomeCategory category = prepareTest(parentCategory.getId());

        IncomeCategory dbIncomeCategory = repository.getByIdAndUserId(category.getId(), category.getUserId());
        asserts(category, dbIncomeCategory);
    }

    @Test
    void findByNameIgnoreCase() {
        IncomeCategory parentCategory = prepareTest(null);
        IncomeCategory category = prepareTest(parentCategory.getId());

        IncomeCategory dbIncomeCategory = repository.findByNameIgnoreCase(category.getName()).orElseThrow(
                () -> new NotFoundObjectException("Object not found"));
        asserts(category, dbIncomeCategory);
    }

    private void asserts(IncomeCategory incomeCategory, IncomeCategory dbIncomeCategory) {
        assertEquals(incomeCategory.getId(), dbIncomeCategory.getId());
        assertEquals(incomeCategory.getParentId(), dbIncomeCategory.getParentId());
        assertEquals(incomeCategory.isHide(), dbIncomeCategory.isHide());
        assertEquals(incomeCategory.getName(), dbIncomeCategory.getName());
        assertEquals(incomeCategory.getUserId(), dbIncomeCategory.getUserId());
    }

    private IncomeCategory prepareTest(UUID parentId) {
        User user = userBuilder.buildUser();
        return incomeCategoryBuilder.buildEntity(RandomStringUtils.randomAlphabetic(20), user.getId(), parentId);
    }
}