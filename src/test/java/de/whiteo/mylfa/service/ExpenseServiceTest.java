package de.whiteo.mylfa.service;

import de.whiteo.mylfa.builder.CurrencyTypeBuilder;
import de.whiteo.mylfa.builder.ExpenseBuilder;
import de.whiteo.mylfa.builder.ExpenseCategoryBuilder;
import de.whiteo.mylfa.builder.UserBuilder;
import de.whiteo.mylfa.domain.CurrencyType;
import de.whiteo.mylfa.domain.ExpenseCategory;
import de.whiteo.mylfa.domain.User;
import de.whiteo.mylfa.dto.expense.ExpenseCreateOrUpdateRequest;
import de.whiteo.mylfa.dto.expense.ExpenseResponse;
import de.whiteo.mylfa.repository.CurrencyTypeRepository;
import de.whiteo.mylfa.repository.ExpenseCategoryRepository;
import de.whiteo.mylfa.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import jakarta.transaction.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ExpenseServiceTest {

    @Autowired
    private ExpenseCategoryRepository expenseCategoryRepository;
    @Autowired
    private CurrencyTypeRepository currencyTypeRepository;
    private ExpenseCategoryBuilder expenseCategoryBuilder;
    private CurrencyTypeBuilder currencyTypeBuilder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ExpenseService service;
    private UserBuilder userBuilder;

    @PostConstruct
    void initialize() {
        expenseCategoryBuilder = new ExpenseCategoryBuilder(expenseCategoryRepository);
        currencyTypeBuilder = new CurrencyTypeBuilder(currencyTypeRepository);
        userBuilder = new UserBuilder(userRepository);
    }

    @Test
    @Transactional
    void create() {
        User user = userBuilder.buildUser();

        ExpenseCreateOrUpdateRequest request = prepareTest(RandomStringUtils.randomAlphabetic(20), user.getId());

        ExpenseResponse response = service.create(user.getEmail(), request);
        asserts(response, request);
    }

    @Test
    @Transactional
    void update() {
        User user = userBuilder.buildUser();

        String randomCurrencyTypeName = RandomStringUtils.randomAlphabetic(20);

        ExpenseCreateOrUpdateRequest request = prepareTest(randomCurrencyTypeName, user.getId());

        ExpenseResponse createResponse = service.create(user.getEmail(), request);
        ExpenseCreateOrUpdateRequest updateRequest = prepareTest(randomCurrencyTypeName, user.getId());
        ExpenseResponse response = service.update(user.getEmail(), createResponse.getId(), updateRequest);
        asserts(response, updateRequest);
    }

    @Test
    @Transactional
    void findById() {
        User user = userBuilder.buildUser();

        ExpenseCreateOrUpdateRequest request = prepareTest(RandomStringUtils.randomAlphabetic(20), user.getId());

        ExpenseResponse createResponse = service.create(user.getEmail(), request);
        ExpenseResponse response = service.findById(user.getEmail(), createResponse.getId());
        asserts(response, request);
    }

    @Test
    @Transactional
    void delete() {
        User user = userBuilder.buildUser();

        ExpenseCreateOrUpdateRequest request = prepareTest(RandomStringUtils.randomAlphabetic(20), user.getId());

        ExpenseResponse createResponse = service.create(user.getEmail(), request);
        service.delete(user.getEmail(), createResponse.getId());
    }

    private void asserts(ExpenseResponse response, ExpenseCreateOrUpdateRequest request) {
        assertEquals(response.getCategory().getId(), request.getCategoryId());
        assertEquals(response.getDescription(), request.getDescription());
        assertEquals(response.getAmount(), request.getAmount());
        assertEquals(response.getCurrencyType().getId(), request.getCurrencyTypeId());
    }

    private ExpenseCreateOrUpdateRequest prepareTest(String currencyName, UUID userId) {
        CurrencyType currencyType = currencyTypeBuilder.buildEntity(currencyName, userId);

        ExpenseCategory expenseCategory = expenseCategoryBuilder.buildEntity(
                RandomStringUtils.randomAlphabetic(20), userId, null);

        return ExpenseBuilder.buildRequest(expenseCategory.getId(), currencyType.getId());
    }
}