package de.whiteo.mylfa.service;

import de.whiteo.mylfa.builder.CurrencyTypeBuilder;
import de.whiteo.mylfa.builder.IncomeBuilder;
import de.whiteo.mylfa.builder.IncomeCategoryBuilder;
import de.whiteo.mylfa.builder.UserBuilder;
import de.whiteo.mylfa.domain.CurrencyType;
import de.whiteo.mylfa.domain.IncomeCategory;
import de.whiteo.mylfa.domain.User;
import de.whiteo.mylfa.dto.income.IncomeCreateOrUpdateRequest;
import de.whiteo.mylfa.dto.income.IncomeResponse;
import de.whiteo.mylfa.repository.CurrencyTypeRepository;
import de.whiteo.mylfa.repository.IncomeCategoryRepository;
import de.whiteo.mylfa.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class IncomeServiceTest {

    @Autowired
    private IncomeCategoryRepository incomeCategoryRepository;
    @Autowired
    private CurrencyTypeRepository currencyTypeRepository;
    private IncomeCategoryBuilder incomeCategoryBuilder;
    private CurrencyTypeBuilder currencyTypeBuilder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IncomeService service;
    private UserBuilder userBuilder;

    @PostConstruct
    void initialize() {
        incomeCategoryBuilder = new IncomeCategoryBuilder(incomeCategoryRepository);
        currencyTypeBuilder = new CurrencyTypeBuilder(currencyTypeRepository);
        userBuilder = new UserBuilder(userRepository);
    }

    @Test
    @Transactional
    void create() {
        User user = userBuilder.buildUser();

        IncomeCreateOrUpdateRequest request = prepareTest(RandomStringUtils.randomAlphabetic(20), user.getId());

        IncomeResponse response = service.create(user.getEmail(), request);
        asserts(response, request);
    }

    @Test
    @Transactional
    void update() {
        User user = userBuilder.buildUser();

        String randomCurrencyTypeName = RandomStringUtils.randomAlphabetic(20);

        IncomeCreateOrUpdateRequest request = prepareTest(randomCurrencyTypeName, user.getId());

        IncomeResponse createResponse = service.create(user.getEmail(), request);
        IncomeCreateOrUpdateRequest updateRequest = prepareTest(randomCurrencyTypeName, user.getId());
        IncomeResponse response = service.update(user.getEmail(), createResponse.getId(), updateRequest);
        asserts(response, updateRequest);
    }

    @Test
    @Transactional
    void findById() {
        User user = userBuilder.buildUser();

        IncomeCreateOrUpdateRequest request = prepareTest(RandomStringUtils.randomAlphabetic(20), user.getId());

        IncomeResponse createResponse = service.create(user.getEmail(), request);
        IncomeResponse response = service.findById(user.getEmail(), createResponse.getId());
        asserts(response, request);
    }

    @Test
    @Transactional
    void delete() {
        User user = userBuilder.buildUser();

        IncomeCreateOrUpdateRequest request = prepareTest(RandomStringUtils.randomAlphabetic(20), user.getId());

        IncomeResponse createResponse = service.create(user.getEmail(), request);
        service.delete(user.getEmail(), createResponse.getId());
    }

    private void asserts(IncomeResponse response, IncomeCreateOrUpdateRequest request) {
        assertEquals(response.getCategory().getId(), request.getCategoryId());
        assertEquals(response.getDescription(), request.getDescription());
        assertEquals(response.getCurrencyType().getId(), request.getCurrencyTypeId());
        assertEquals(response.getAmount(), request.getAmount());
    }

    private IncomeCreateOrUpdateRequest prepareTest(String currencyName, UUID userId) {
        CurrencyType currencyType = currencyTypeBuilder.buildEntity(currencyName, userId);

        IncomeCategory incomeCategory = incomeCategoryBuilder.
                buildEntity(RandomStringUtils.randomAlphabetic(20), userId, null);

        return IncomeBuilder.buildRequest(incomeCategory.getId(), currencyType.getId());
    }
}