package de.whiteo.mylfa.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.whiteo.mylfa.builder.CurrencyTypeBuilder;
import de.whiteo.mylfa.builder.UserBuilder;
import de.whiteo.mylfa.domain.CurrencyType;
import de.whiteo.mylfa.domain.User;
import de.whiteo.mylfa.dto.currencytype.CurrencyTypeCreateOrUpdateRequest;
import de.whiteo.mylfa.dto.currencytype.CurrencyTypeFindAllRequest;
import de.whiteo.mylfa.helper.TokenHelper;
import de.whiteo.mylfa.repository.CurrencyTypeRepository;
import de.whiteo.mylfa.repository.UserRepository;
import de.whiteo.mylfa.security.TokenInteract;
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
class CurrencyTypeRestControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private CurrencyTypeRepository repository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenInteract tokenInteract;
    @Autowired
    private ObjectMapper objectMapper;
    private CurrencyTypeBuilder builder;
    private TokenHelper tokenHelper;
    private UserBuilder userBuilder;
    private MockMvc mockMvc;

    @PostConstruct
    void initialize() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        builder = new CurrencyTypeBuilder(repository);
        tokenHelper = new TokenHelper(tokenInteract);
        userBuilder = new UserBuilder(userRepository);
    }

    @Test
    @Transactional
    void create_successful() throws Exception {
        User user = userBuilder.buildUser();

        prepareTest(RandomStringUtils.randomAlphabetic(20), user);

        CurrencyTypeCreateOrUpdateRequest request = CurrencyTypeBuilder.buildRequest(
                RandomStringUtils.randomAlphabetic(20));

        mockMvc.perform(post("/api/v1/currency-type/create")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", tokenHelper.getToken(user.getEmail())))
                .andExpect(status().isCreated());
    }

    @Test
    @Transactional
    void create_unsuccessful() throws Exception {
        User user = userBuilder.buildUser();

        String randomString = RandomStringUtils.randomAlphabetic(20);
        prepareTest(randomString, user);

        CurrencyTypeCreateOrUpdateRequest request = CurrencyTypeBuilder.buildRequest(randomString);

        mockMvc.perform(post("/api/v1/currency-type/create")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", tokenHelper.getToken(user.getEmail())))
                .andExpect(status().isConflict());
    }

    @Test
    @Transactional
    void delete_successful() throws Exception {
        User user = userBuilder.buildUser();

        CurrencyType currencyType = prepareTest(RandomStringUtils.randomAlphabetic(20), user);

        mockMvc.perform(delete(String.format("/api/v1/currency-type/delete/%s", currencyType.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", tokenHelper.getToken(user.getEmail())))
                .andExpect(status().isNoContent());
    }

    @Test
    @Transactional
    void delete_unsuccessful() throws Exception {
        User user = userBuilder.buildUser();

        mockMvc.perform(delete(String.format("/api/v1/currency-type/delete/%s", UUID.randomUUID()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", tokenHelper.getToken(user.getEmail())))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void update_successful() throws Exception {
        User user = userBuilder.buildUser();

        CurrencyType currencyType = prepareTest(RandomStringUtils.randomAlphabetic(20), user);

        CurrencyTypeCreateOrUpdateRequest request = CurrencyTypeBuilder.buildRequest(
                RandomStringUtils.randomAlphabetic(20));

        mockMvc.perform(put(String.format("/api/v1/currency-type/edit/%s", currencyType.getId()))
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", tokenHelper.getToken(user.getEmail())))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    void update_unsuccessful() throws Exception {
        User user = userBuilder.buildUser();

        String randomString = RandomStringUtils.randomAlphabetic(20);
        CurrencyType currencyType = prepareTest(randomString, user);

        CurrencyTypeCreateOrUpdateRequest request = CurrencyTypeBuilder.buildRequest(randomString);

        mockMvc.perform(put(String.format("/api/v1/currency-type/edit/%s", currencyType.getId()))
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", tokenHelper.getToken(user.getEmail())))
                .andExpect(status().isConflict());
    }

    @Test
    @Transactional
    void find_successful() throws Exception {
        User user = userBuilder.buildUser();

        CurrencyType currencyType = prepareTest(RandomStringUtils.randomAlphabetic(20), user);

        mockMvc.perform(get(String.format("/api/v1/currency-type/find/%s", currencyType.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", tokenHelper.getToken(user.getEmail())))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    void find_unsuccessful() throws Exception {
        User user = userBuilder.buildUser();

        mockMvc.perform(get(String.format("/api/v1/currency-type/%s", UUID.randomUUID()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", tokenHelper.getToken(user.getEmail())))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void find_all_successful() throws Exception {
        User user = userBuilder.buildUser();

        for (int i = 0; i < 10; i++) {
            prepareTest(RandomStringUtils.randomAlphabetic(20), user);
        }

        CurrencyTypeFindAllRequest request = CurrencyTypeBuilder.buildFindAllRequest();

        mockMvc.perform(post("/api/v1/currency-type/find")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", tokenHelper.getToken(user.getEmail())))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    void find_empty_successful() throws Exception {
        User user = userBuilder.buildUser();

        CurrencyTypeFindAllRequest request = CurrencyTypeBuilder.buildFindAllRequest();

        mockMvc.perform(post("/api/v1/currency-type/find")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", tokenHelper.getToken(user.getEmail())))
                .andExpect(status().isOk());
    }

    private CurrencyType prepareTest(String name, User user) {
        return builder.buildEntity(name, user.getId());
    }
}