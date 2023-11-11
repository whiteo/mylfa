package de.whiteo.mylfa.repository;

import de.whiteo.mylfa.builder.CurrencyTypeBuilder;
import de.whiteo.mylfa.builder.ExpenseBuilder;
import de.whiteo.mylfa.builder.ExpenseCategoryBuilder;
import de.whiteo.mylfa.builder.UserBuilder;
import de.whiteo.mylfa.domain.CurrencyType;
import de.whiteo.mylfa.domain.Expense;
import de.whiteo.mylfa.domain.ExpenseCategory;
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
class ExpenseRepositoryTest {

    @Autowired
    private ExpenseCategoryRepository expenseCategoryRepository;
    @Autowired
    private CurrencyTypeRepository currencyTypeRepository;
    private ExpenseCategoryBuilder expenseCategoryBuilder;
    @Autowired
    private ExpenseRepository repository;
    private CurrencyTypeBuilder currencyTypeBuilder;
    @Autowired
    private UserRepository userRepository;
    private ExpenseBuilder expenseBuilder;
    private UserBuilder userBuilder;

    @PostConstruct
    void initialize() {
        expenseCategoryBuilder = new ExpenseCategoryBuilder(expenseCategoryRepository);
        currencyTypeBuilder = new CurrencyTypeBuilder(currencyTypeRepository);
        expenseBuilder = new ExpenseBuilder(repository);
        userBuilder = new UserBuilder(userRepository);
    }

    @Test
    void get() {
        Expense expense = prepareTest();
        Expense dbExpense = repository.getByIdAndUserId(expense.getId(), expense.getUserId());
        asserts(expense, dbExpense);
    }

    private void asserts(Expense expense, Expense dbExpense) {
        assertEquals(expense.getId(), dbExpense.getId());
        assertEquals(expense.getDescription(), dbExpense.getDescription());
        assertEquals(expense.getCategoryId(), dbExpense.getCategoryId());
        assertEquals(expense.getAmount(), dbExpense.getAmount());
        assertEquals(expense.getUserId(), dbExpense.getUserId());
    }

    private Expense prepareTest() {
        UUID userId = userBuilder.buildUser().getId();

        CurrencyType currencyType = currencyTypeBuilder.buildEntity(
                RandomStringUtils.randomAlphabetic(20), userId);

        ExpenseCategory expenseCategory = expenseCategoryBuilder.buildEntity(
                RandomStringUtils.randomAlphabetic(20), userId, null);

        return expenseBuilder.buildEntity(userId, expenseCategory.getId(), currencyType.getId());
    }
}