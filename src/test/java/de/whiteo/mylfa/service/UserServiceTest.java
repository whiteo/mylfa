package de.whiteo.mylfa.service;

import de.whiteo.mylfa.builder.UserBuilder;
import de.whiteo.mylfa.dto.user.UserCreateRequest;
import de.whiteo.mylfa.dto.user.UserResponse;
import de.whiteo.mylfa.dto.user.UserUpdatePasswordRequest;
import de.whiteo.mylfa.dto.user.UserUpdateRequest;
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
class UserServiceTest {

    @Autowired
    private UserService service;
    private final static String MAIL = "@test.com";

    @Test
    @Transactional
    void create() {
        UserCreateRequest request = UserBuilder.buildUserCreateRequest(
                RandomStringUtils.randomAlphabetic(5) + MAIL, RandomStringUtils.randomAlphabetic(20));

        UserResponse response = service.create(request);
        asserts(response, request.getEmail());
    }

    @Test
    @Transactional
    void update() {
        String passwd = RandomStringUtils.randomAlphabetic(20);

        UserCreateRequest createRequest = UserBuilder.buildUserCreateRequest(
                RandomStringUtils.randomAlphabetic(5) + MAIL, passwd);
        UserResponse createResponse = service.create(createRequest);

        UserUpdateRequest updateRequest = UserBuilder.buildUserUpdateRequest(
                RandomStringUtils.randomAlphabetic(5) + MAIL);
        UserResponse response = service.update(createResponse.getId(), updateRequest);
        asserts(response, updateRequest.getEmail());
    }

    @Test
    @Transactional
    void updatePassword() {
        String passwd = RandomStringUtils.randomAlphabetic(20);

        UserCreateRequest createRequest = UserBuilder.buildUserCreateRequest(
                RandomStringUtils.randomAlphabetic(5) + MAIL, passwd);
        UserResponse createResponse = service.create(createRequest);

        UserUpdatePasswordRequest updateRequest = UserBuilder.buildUserPasswordUpdateRequest(
                RandomStringUtils.randomAlphabetic(20), passwd);
        UserResponse response = service.updatePassword(createResponse.getId(), updateRequest);
        asserts(response, createResponse.getEmail());
    }

    @Test
    @Transactional
    void findById() {
        UserCreateRequest request = UserBuilder.buildUserCreateRequest(
                RandomStringUtils.randomAlphabetic(5) + MAIL,
                RandomStringUtils.randomAlphabetic(20));

        UserResponse createResponse = service.create(request);
        UserResponse response = service.find(createResponse.getId());
        asserts(response, createResponse.getEmail());
    }

    @SuppressWarnings("java:S2699")
    @Test
    @Transactional
    void delete() {
        UserCreateRequest request = UserBuilder.buildUserCreateRequest(
                RandomStringUtils.randomAlphabetic(5) + MAIL,
                RandomStringUtils.randomAlphabetic(20));

        UserResponse createRequest = service.create(request);
        service.delete(createRequest.getId());
    }

    private void asserts(UserResponse response, String email) {
        assertEquals(response.getEmail(), email);
    }
}