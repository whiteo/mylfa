package de.whiteo.mylfa.repository;

import de.whiteo.mylfa.builder.CurrencyTypeBuilder;
import de.whiteo.mylfa.builder.IncomeBuilder;
import de.whiteo.mylfa.builder.IncomeCategoryBuilder;
import de.whiteo.mylfa.builder.UserBuilder;
import de.whiteo.mylfa.domain.CurrencyType;
import de.whiteo.mylfa.domain.Income;
import de.whiteo.mylfa.domain.IncomeCategory;
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
class IncomeRepositoryTest {

    @Autowired
    private IncomeCategoryRepository incomeCategoryRepository;
    @Autowired
    private CurrencyTypeRepository currencyTypeRepository;
    private IncomeCategoryBuilder incomeCategoryBuilder;
    @Autowired
    private IncomeRepository repository;
    private CurrencyTypeBuilder currencyTypeBuilder;
    @Autowired
    private UserRepository userRepository;
    private IncomeBuilder incomeBuilder;
    private UserBuilder userBuilder;

    @PostConstruct
    void initialize() {
        incomeCategoryBuilder = new IncomeCategoryBuilder(incomeCategoryRepository);
        currencyTypeBuilder = new CurrencyTypeBuilder(currencyTypeRepository);
        incomeBuilder = new IncomeBuilder(repository);
        userBuilder = new UserBuilder(userRepository);
    }

    @Test
    void get() {
        Income income = prepareTest();
        Income dbIncome = repository.getByIdAndUserId(income.getId(), income.getUserId());
        asserts(income, dbIncome);
    }

    private void asserts(Income income, Income dbIncome) {
        assertEquals(income.getId(), dbIncome.getId());
        assertEquals(income.getDescription(), dbIncome.getDescription());
        assertEquals(income.getCategoryId(), dbIncome.getCategoryId());
        assertEquals(income.getAmount(), dbIncome.getAmount());
        assertEquals(income.getUserId(), dbIncome.getUserId());
    }

    private Income prepareTest() {
        UUID userId = userBuilder.buildUser().getId();

        CurrencyType currencyType = currencyTypeBuilder.buildEntity(
                RandomStringUtils.randomAlphabetic(20), userId);

        IncomeCategory incomeCategory = incomeCategoryBuilder.
                buildEntity(RandomStringUtils.randomAlphabetic(20), userId, null);

        return incomeBuilder.buildEntity(userId, incomeCategory.getId(), currencyType.getId());
    }
}