package de.whiteo.mylfa.builder;

import de.whiteo.mylfa.domain.User;
import de.whiteo.mylfa.dto.user.UserCreateRequest;
import de.whiteo.mylfa.dto.user.UserLoginRequest;
import de.whiteo.mylfa.dto.user.UserUpdatePasswordRequest;
import de.whiteo.mylfa.dto.user.UserUpdatePropertiesRequest;
import de.whiteo.mylfa.dto.user.UserUpdateRequest;
import de.whiteo.mylfa.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@RequiredArgsConstructor
public class UserBuilder {

    public final static String EMAIL = "test1@test.com";
    public final static String PASSWORD = "test1";

    private final UserRepository repository;

    public static UserLoginRequest buildUserLoginRequest(String email, String passwd) {
        UserLoginRequest request = new UserLoginRequest();
        request.setEmail(email);
        request.setPassword(passwd);
        return request;
    }

    public static UserCreateRequest buildUserCreateRequest(String email, String passwd) {
        UserCreateRequest request = new UserCreateRequest();
        Map<String, String> properties = new HashMap<>();
        properties.put("lang", "eng");
        request.setProperties(properties);
        request.setEmail(email);
        request.setPassword(passwd);
        return request;
    }

    public static UserUpdatePasswordRequest buildUserPasswordUpdateRequest(String passwd, String oldPasswd) {
        UserUpdatePasswordRequest request = new UserUpdatePasswordRequest();
        request.setPassword(passwd);
        request.setOldPassword(oldPasswd);
        return request;
    }

    public static UserUpdateRequest buildUserUpdateRequest(String email) {
        UserUpdateRequest request = new UserUpdateRequest();
        request.setEmail(email);
        return request;
    }

    public static UserUpdatePropertiesRequest buildUserUpdatePropertiesRequest(Map<String, String> properties) {
        UserUpdatePropertiesRequest request = new UserUpdatePropertiesRequest();
        request.setProperties(properties);
        return request;
    }

    public User buildUser() {
        User user = repository.findByEmailIgnoreCase(EMAIL).orElse(new User());
        Map<String, String> properties = new HashMap<>();
        properties.put("lang", "eng");
        user.setProperties(properties);
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD.toCharArray());
        return repository.save(user);
    }
}