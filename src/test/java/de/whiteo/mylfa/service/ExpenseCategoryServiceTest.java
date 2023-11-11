package de.whiteo.mylfa.service;

import de.whiteo.mylfa.builder.ExpenseCategoryBuilder;
import de.whiteo.mylfa.builder.UserBuilder;
import de.whiteo.mylfa.domain.User;
import de.whiteo.mylfa.dto.expensecategory.ExpenseCategoryCreateOrUpdateRequest;
import de.whiteo.mylfa.dto.expensecategory.ExpenseCategoryResponse;
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
class ExpenseCategoryServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ExpenseCategoryService service;
    private UserBuilder userBuilder;

    @PostConstruct
    void initialize() {
        userBuilder = new UserBuilder(userRepository);
    }

    @Test
    @Transactional
    void create() {
        User user = userBuilder.buildUser();

        ExpenseCategoryCreateOrUpdateRequest request = ExpenseCategoryBuilder.buildRequest(
                RandomStringUtils.randomAlphabetic(20));
        ExpenseCategoryResponse response = service.create(user.getEmail(), request);
        asserts(response, request, false);
    }

    @Test
    @Transactional
    void update() {
        User user = userBuilder.buildUser();

        ExpenseCategoryCreateOrUpdateRequest createRequest = ExpenseCategoryBuilder.buildRequest(
                RandomStringUtils.randomAlphabetic(20));
        ExpenseCategoryResponse createResponse = service.create(user.getEmail(), createRequest);

        ExpenseCategoryCreateOrUpdateRequest updateRequest = ExpenseCategoryBuilder.buildRequest(
                RandomStringUtils.randomAlphabetic(20));
        ExpenseCategoryResponse response = service.update(user.getEmail(), createResponse.getId(), updateRequest);
        asserts(response, updateRequest, false);
    }

    @Test
    @Transactional
    void find_by_id() {
        User user = userBuilder.buildUser();

        ExpenseCategoryCreateOrUpdateRequest request = ExpenseCategoryBuilder.buildRequest(
                RandomStringUtils.randomAlphabetic(20));

        ExpenseCategoryResponse createResponse = service.create(user.getEmail(), request);
        ExpenseCategoryResponse response = service.findById(user.getEmail(), createResponse.getId());
        asserts(response, request, false);
    }

    @Test
    @Transactional
    void create_with_parent() {
        User user = userBuilder.buildUser();

        ExpenseCategoryCreateOrUpdateRequest parentRequest = ExpenseCategoryBuilder.buildRequest(
                RandomStringUtils.randomAlphabetic(20));
        ExpenseCategoryResponse parentResponse = service.create(user.getEmail(), parentRequest);

        ExpenseCategoryCreateOrUpdateRequest request = ExpenseCategoryBuilder.buildRequestWithParentId(
                RandomStringUtils.randomAlphabetic(20), parentResponse.getId());
        ExpenseCategoryResponse response = service.create(user.getEmail(), request);
        asserts(response, request, true);
    }

    @Test
    @Transactional
    void update_with_parent() {
        User user = userBuilder.buildUser();

        ExpenseCategoryCreateOrUpdateRequest parentRequest = ExpenseCategoryBuilder.buildRequest(
                RandomStringUtils.randomAlphabetic(20));
        ExpenseCategoryResponse parentResponse = service.create(user.getEmail(), parentRequest);

        ExpenseCategoryCreateOrUpdateRequest createRequest = ExpenseCategoryBuilder.buildRequestWithParentId(
                RandomStringUtils.randomAlphabetic(20), parentResponse.getId());
        ExpenseCategoryResponse createResponse = service.create(user.getEmail(), createRequest);

        ExpenseCategoryCreateOrUpdateRequest updateParentRequest = ExpenseCategoryBuilder.buildRequest(
                RandomStringUtils.randomAlphabetic(20));
        ExpenseCategoryResponse updateParentResponse = service.create(user.getEmail(), updateParentRequest);

        ExpenseCategoryCreateOrUpdateRequest updateRequest = ExpenseCategoryBuilder.buildRequestWithParentId(
                RandomStringUtils.randomAlphabetic(20), updateParentResponse.getId());
        ExpenseCategoryResponse response = service.update(user.getEmail(), createResponse.getId(), updateRequest);
        asserts(response, updateRequest, true);
    }

    @Test
    @Transactional
    void find_by_id_with_parent() {
        User user = userBuilder.buildUser();

        ExpenseCategoryCreateOrUpdateRequest parentRequest = ExpenseCategoryBuilder.buildRequest(
                RandomStringUtils.randomAlphabetic(20));
        ExpenseCategoryResponse parentResponse = service.create(user.getEmail(), parentRequest);

        ExpenseCategoryCreateOrUpdateRequest request = ExpenseCategoryBuilder.buildRequestWithParentId(
                RandomStringUtils.randomAlphabetic(20), parentResponse.getId());
        ExpenseCategoryResponse createResponse = service.create(user.getEmail(), request);

        ExpenseCategoryResponse response = service.findById(user.getEmail(), createResponse.getId());
        asserts(response, request, true);
    }

    @SuppressWarnings("java:S2699")
    @Test
    @Transactional
    void delete() {
        User user = userBuilder.buildUser();

        ExpenseCategoryCreateOrUpdateRequest request = ExpenseCategoryBuilder.buildRequest(
                RandomStringUtils.randomAlphabetic(20));

        ExpenseCategoryResponse createResponse = service.create(user.getEmail(), request);
        service.delete(user.getEmail(), createResponse.getId());
    }

    private void asserts(ExpenseCategoryResponse response, ExpenseCategoryCreateOrUpdateRequest request,
                         boolean withParent) {
        assertEquals(response.getName(), request.getName());
        assertEquals(response.isHide(), request.isHide());
        if (withParent) {
            assertEquals(response.getParent().getId(), request.getParentId());
        }
    }
}