package de.whiteo.mylfa.service;

import de.whiteo.mylfa.builder.IncomeCategoryBuilder;
import de.whiteo.mylfa.builder.UserBuilder;
import de.whiteo.mylfa.domain.User;
import de.whiteo.mylfa.dto.incomecategory.IncomeCategoryCreateOrUpdateRequest;
import de.whiteo.mylfa.dto.incomecategory.IncomeCategoryResponse;
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
class IncomeCategoryServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IncomeCategoryService service;
    private UserBuilder userBuilder;

    @PostConstruct
    void initialize() {
        userBuilder = new UserBuilder(userRepository);
    }

    @Test
    @Transactional
    void create() {
        User user = userBuilder.buildUser();

        IncomeCategoryCreateOrUpdateRequest request = IncomeCategoryBuilder.buildRequest(
                RandomStringUtils.randomAlphabetic(20));
        IncomeCategoryResponse response = service.create(user.getEmail(), request);
        asserts(response, request, false);
    }

    @Test
    @Transactional
    void update() {
        User user = userBuilder.buildUser();

        IncomeCategoryCreateOrUpdateRequest request = IncomeCategoryBuilder.buildRequest(
                RandomStringUtils.randomAlphabetic(20));
        IncomeCategoryResponse createResponse = service.create(user.getEmail(), request);

        IncomeCategoryCreateOrUpdateRequest updateRequest = IncomeCategoryBuilder.buildRequest(
                RandomStringUtils.randomAlphabetic(20));
        IncomeCategoryResponse response = service.update(user.getEmail(), createResponse.getId(), updateRequest);
        asserts(response, updateRequest, false);
    }

    @Test
    @Transactional
    void find_by_id() {
        User user = userBuilder.buildUser();

        IncomeCategoryCreateOrUpdateRequest request = IncomeCategoryBuilder.buildRequest(
                RandomStringUtils.randomAlphabetic(20));

        IncomeCategoryResponse createResponse = service.create(user.getEmail(), request);
        IncomeCategoryResponse response = service.findById(user.getEmail(), createResponse.getId());
        asserts(response, request, false);
    }

    @Test
    @Transactional
    void create_with_parent() {
        User user = userBuilder.buildUser();

        IncomeCategoryCreateOrUpdateRequest parentRequest =
                IncomeCategoryBuilder.buildRequest(RandomStringUtils.randomAlphabetic(20));
        IncomeCategoryResponse parentResponse = service.create(user.getEmail(), parentRequest);

        IncomeCategoryCreateOrUpdateRequest request = IncomeCategoryBuilder.buildRequestWithParentId(
                RandomStringUtils.randomAlphabetic(20), parentResponse.getId());
        IncomeCategoryResponse response = service.create(user.getEmail(), request);
        asserts(response, request, true);
    }

    @Test
    @Transactional
    void update_with_parent() {
        User user = userBuilder.buildUser();

        IncomeCategoryCreateOrUpdateRequest parentRequest =
                IncomeCategoryBuilder.buildRequest(RandomStringUtils.randomAlphabetic(20));
        IncomeCategoryResponse parentResponse = service.create(user.getEmail(), parentRequest);

        IncomeCategoryCreateOrUpdateRequest createRequest = IncomeCategoryBuilder.buildRequestWithParentId(
                RandomStringUtils.randomAlphabetic(20), parentResponse.getId());
        IncomeCategoryResponse createResponse = service.create(user.getEmail(), createRequest);

        IncomeCategoryCreateOrUpdateRequest updateParentRequest =
                IncomeCategoryBuilder.buildRequest(RandomStringUtils.randomAlphabetic(20));
        IncomeCategoryResponse updateParentResponse = service.create(user.getEmail(), updateParentRequest);

        IncomeCategoryCreateOrUpdateRequest updateRequest = IncomeCategoryBuilder.buildRequestWithParentId(
                RandomStringUtils.randomAlphabetic(20), updateParentResponse.getId());
        IncomeCategoryResponse response = service.update(user.getEmail(), createResponse.getId(), updateRequest);
        asserts(response, updateRequest, true);
    }

    @Test
    @Transactional
    void find_by_id_with_parent() {
        User user = userBuilder.buildUser();

        IncomeCategoryCreateOrUpdateRequest parentRequest =
                IncomeCategoryBuilder.buildRequest(RandomStringUtils.randomAlphabetic(20));
        IncomeCategoryResponse parentResponse = service.create(user.getEmail(), parentRequest);

        IncomeCategoryCreateOrUpdateRequest request = IncomeCategoryBuilder.buildRequestWithParentId(
                RandomStringUtils.randomAlphabetic(20), parentResponse.getId());
        IncomeCategoryResponse createResponse = service.create(user.getEmail(), request);

        IncomeCategoryResponse response = service.findById(user.getEmail(), createResponse.getId());
        asserts(response, request, true);
    }

    @SuppressWarnings("java:S2699")
    @Test
    @Transactional
    void delete() {
        User user = userBuilder.buildUser();

        IncomeCategoryCreateOrUpdateRequest request = IncomeCategoryBuilder.buildRequest(
                RandomStringUtils.randomAlphabetic(20));

        IncomeCategoryResponse createResponse = service.create(user.getEmail(), request);
        service.delete(user.getEmail(), createResponse.getId());
    }

    private void asserts(IncomeCategoryResponse response, IncomeCategoryCreateOrUpdateRequest request,
                         boolean withParent) {
        assertEquals(response.getName(), request.getName());
        assertEquals(response.isHide(), request.isHide());
        if (withParent) {
            assertEquals(response.getParent().getId(), request.getParentId());
        }
    }
}