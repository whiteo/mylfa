package de.whiteo.mylfa.service;

import de.whiteo.mylfa.builder.CurrencyTypeBuilder;
import de.whiteo.mylfa.builder.UserBuilder;
import de.whiteo.mylfa.domain.User;
import de.whiteo.mylfa.dto.currencytype.CurrencyTypeCreateOrUpdateRequest;
import de.whiteo.mylfa.dto.currencytype.CurrencyTypeResponse;
import de.whiteo.mylfa.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CurrencyTypeServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CurrencyTypeService service;
    private UserBuilder userBuilder;

    @PostConstruct
    void initialize() {
        userBuilder = new UserBuilder(userRepository);
    }

    @Test
    @Transactional
    void create() {
        User user = userBuilder.buildUser();

        CurrencyTypeCreateOrUpdateRequest request = CurrencyTypeBuilder.buildRequest(
                RandomStringUtils.randomAlphabetic(20));

        CurrencyTypeResponse response = service.create(user.getEmail(), request);
        asserts(response, request);
    }

    @Test
    @Transactional
    void update() {
        User user = userBuilder.buildUser();

        CurrencyTypeCreateOrUpdateRequest request = CurrencyTypeBuilder.buildRequest(
                RandomStringUtils.randomAlphabetic(20));

        CurrencyTypeResponse createResponse = service.create(user.getEmail(), request);
        CurrencyTypeCreateOrUpdateRequest updateRequest = CurrencyTypeBuilder.buildRequest(
                RandomStringUtils.randomAlphabetic(20));
        CurrencyTypeResponse response = service.update(user.getEmail(), createResponse.getId(), updateRequest);
        asserts(response, updateRequest);
    }

    @Test
    @Transactional
    void findById() {
        User user = userBuilder.buildUser();

        CurrencyTypeCreateOrUpdateRequest request = CurrencyTypeBuilder.buildRequest(
                RandomStringUtils.randomAlphabetic(20));

        CurrencyTypeResponse createResponse = service.create(user.getEmail(), request);
        CurrencyTypeResponse response = service.findById(user.getEmail(), createResponse.getId());
        asserts(response, request);
    }

    @SuppressWarnings("java:S2699")
    @Test
    @Transactional
    void delete() {
        User user = userBuilder.buildUser();

        CurrencyTypeCreateOrUpdateRequest request = CurrencyTypeBuilder.buildRequest(
                RandomStringUtils.randomAlphabetic(20));

        CurrencyTypeResponse createResponse = service.create(user.getEmail(), request);
        service.delete(user.getEmail(), createResponse.getId());
    }

    private void asserts(CurrencyTypeResponse response, CurrencyTypeCreateOrUpdateRequest request) {
        assertEquals(response.getName(), request.getName());
        assertEquals(response.isHide(), request.isHide());
    }
}