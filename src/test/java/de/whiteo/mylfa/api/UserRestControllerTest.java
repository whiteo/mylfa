package de.whiteo.mylfa.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.whiteo.mylfa.builder.UserBuilder;
import de.whiteo.mylfa.domain.User;
import de.whiteo.mylfa.dto.user.UserCreateRequest;
import de.whiteo.mylfa.dto.user.UserUpdatePasswordRequest;
import de.whiteo.mylfa.dto.user.UserUpdatePropertiesRequest;
import de.whiteo.mylfa.dto.user.UserUpdateRequest;
import de.whiteo.mylfa.helper.TokenHelper;
import de.whiteo.mylfa.repository.UserRepository;
import de.whiteo.mylfa.util.JwtTokenUtil;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRestControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private UserRepository repository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    private TokenHelper tokenHelper;
    private UserBuilder builder;
    private MockMvc mockMvc;

    @PostConstruct
    void initialize() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        builder = new UserBuilder(repository);
        tokenHelper = new TokenHelper(jwtTokenUtil);
    }

    @Test
    @Transactional
    void create_successful() throws Exception {
        UserCreateRequest request = UserBuilder.buildUserCreateRequest(
                RandomStringUtils.randomAlphabetic(5) + "@test.com", RandomStringUtils.randomAlphabetic(20));

        mockMvc.perform(post("/api/v1/user/create")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @Transactional
    void create_unsuccessful() throws Exception {
        builder.buildUser();

        UserCreateRequest request = UserBuilder.buildUserCreateRequest(UserBuilder.EMAIL, UserBuilder.PASSWORD);

        mockMvc.perform(post("/api/v1/user/create")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    @Transactional
    void delete_successful() throws Exception {
        User user = builder.buildUser();

        mockMvc.perform(delete(String.format("/api/v1/user/%s", user.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", tokenHelper.getToken(user.getEmail())))
                .andExpect(status().isNoContent());
    }

    @Test
    @Transactional
    void delete_unsuccessful() throws Exception {
        mockMvc.perform(delete(String.format("/api/v1/user/%s", UUID.randomUUID()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", tokenHelper.getToken(RandomStringUtils.randomAlphabetic(20))))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void update_successful() throws Exception {
        User user = builder.buildUser();

        UserUpdateRequest request = UserBuilder.buildUserUpdateRequest(
                RandomStringUtils.randomAlphabetic(5) + "@test.com");

        mockMvc.perform(put(String.format("/api/v1/user/%s/edit", user.getId()))
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", tokenHelper.getToken(user.getEmail())))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    void update_unsuccessful() throws Exception {
        User user = builder.buildUser();

        UserUpdateRequest request = UserBuilder.buildUserUpdateRequest(
                RandomStringUtils.randomAlphabetic(5) + "@test.com");

        mockMvc.perform(put(String.format("/api/v1/user/%s/edit", UUID.randomUUID()))
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", tokenHelper.getToken(user.getEmail())))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void update_properties_successful() throws Exception {
        User user = builder.buildUser();

        Map<String, String> properties = new HashMap<>();
        properties.put("lang", "eng");
        properties.put("showHide", "true");

        UserUpdatePropertiesRequest request = UserBuilder.buildUserUpdatePropertiesRequest(properties);

        mockMvc.perform(put(String.format("/api/v1/user/%s/edit-properties", user.getId()))
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", tokenHelper.getToken(user.getEmail())))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    void update_properties_unsuccessful() throws Exception {
        User user = builder.buildUser();

        Map<String, String> properties = new HashMap<>();
        properties.put(RandomStringUtils.randomAlphabetic(3), RandomStringUtils.randomAlphabetic(3));

        UserUpdatePropertiesRequest request = UserBuilder.buildUserUpdatePropertiesRequest(properties);

        mockMvc.perform(put(String.format("/api/v1/user/%s/edit-properties", user.getId()))
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", tokenHelper.getToken(user.getEmail())))
                .andExpect(status().isConflict());
    }
    @Test
    @Transactional
    void update_password_successful() throws Exception {
        User user = builder.buildUser();

        UserUpdatePasswordRequest request = UserBuilder.buildUserPasswordUpdateRequest(
                RandomStringUtils.randomAlphabetic(20),
                String.valueOf(user.getPassword()));

        mockMvc.perform(put(String.format("/api/v1/user/%s/edit-password", user.getId()))
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", tokenHelper.getToken(user.getEmail())))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    void update_password_unsuccessful() throws Exception {
        User user = builder.buildUser();

        UserUpdatePasswordRequest request = UserBuilder.buildUserPasswordUpdateRequest(
                RandomStringUtils.randomAlphabetic(20),
                String.valueOf(user.getPassword()));

        mockMvc.perform(put(String.format("/api/v1/user/%s/edit-password", UUID.randomUUID()))
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", tokenHelper.getToken(user.getEmail())))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void find_successful() throws Exception {
        User user = builder.buildUser();

        mockMvc.perform(get(String.format("/api/v1/user/%s", user.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", tokenHelper.getToken(user.getEmail())))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    void find_unsuccessful() throws Exception {
        User user = builder.buildUser();

        mockMvc.perform(get(String.format("/api/v1/user/%s", UUID.randomUUID()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", tokenHelper.getToken(user.getEmail())))
                .andExpect(status().isNotFound());
    }
}