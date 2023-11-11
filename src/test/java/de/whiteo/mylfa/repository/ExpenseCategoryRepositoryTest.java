package de.whiteo.mylfa.repository;

import de.whiteo.mylfa.builder.ExpenseCategoryBuilder;
import de.whiteo.mylfa.builder.UserBuilder;
import de.whiteo.mylfa.domain.ExpenseCategory;
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
class ExpenseCategoryRepositoryTest {

    private ExpenseCategoryBuilder expenseCategoryBuilder;
    @Autowired
    private ExpenseCategoryRepository repository;
    @Autowired
    private UserRepository userRepository;
    private UserBuilder userBuilder;

    @PostConstruct
    void initialize() {
        expenseCategoryBuilder = new ExpenseCategoryBuilder(repository);
        userBuilder = new UserBuilder(userRepository);
    }

    @Test
    void get() {
        ExpenseCategory parentCategory = prepareTest(null);
        ExpenseCategory category = prepareTest(parentCategory.getId());

        ExpenseCategory dbExpenseCategory = repository.getByIdAndUserId(category.getId(), category.getUserId());
        asserts(category, dbExpenseCategory);
    }

    @Test
    void findByNameIgnoreCase() {
        ExpenseCategory parentCategory = prepareTest(null);
        ExpenseCategory category = prepareTest(parentCategory.getId());
        ExpenseCategory dbExpenseCategory = repository.findByNameIgnoreCase(category.getName()).orElseThrow(
                () -> new NotFoundObjectException("Object not found"));
        asserts(category, dbExpenseCategory);
    }

    private void asserts(ExpenseCategory expenseCategory, ExpenseCategory dbExpenseCategory) {
        assertEquals(expenseCategory.getId(), dbExpenseCategory.getId());
        assertEquals(expenseCategory.getParentId(), dbExpenseCategory.getParentId());
        assertEquals(expenseCategory.isHide(), dbExpenseCategory.isHide());
        assertEquals(expenseCategory.getName(), dbExpenseCategory.getName());
        assertEquals(expenseCategory.getUserId(), dbExpenseCategory.getUserId());
    }

    private ExpenseCategory prepareTest(UUID parentId) {
        User user = userBuilder.buildUser();
        return expenseCategoryBuilder.buildEntity(RandomStringUtils.randomAlphabetic(20), user.getId(), parentId);
    }
}