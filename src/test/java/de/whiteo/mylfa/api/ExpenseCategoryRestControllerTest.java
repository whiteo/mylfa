package de.whiteo.mylfa.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.whiteo.mylfa.builder.ExpenseCategoryBuilder;
import de.whiteo.mylfa.builder.UserBuilder;
import de.whiteo.mylfa.domain.ExpenseCategory;
import de.whiteo.mylfa.domain.User;
import de.whiteo.mylfa.dto.expensecategory.ExpenseCategoryCreateOrUpdateRequest;
import de.whiteo.mylfa.helper.TokenHelper;
import de.whiteo.mylfa.repository.ExpenseCategoryRepository;
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
class ExpenseCategoryRestControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ExpenseCategoryRepository repository;
    private ExpenseCategoryBuilder builder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    private TokenHelper tokenHelper;
    private UserBuilder userBuilder;
    private MockMvc mockMvc;

    @PostConstruct
    void initialize() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        builder = new ExpenseCategoryBuilder(repository);
        tokenHelper = new TokenHelper(jwtTokenUtil);
        userBuilder = new UserBuilder(userRepository);
    }

    @Test
    @Transactional
    void create_successful() throws Exception {
        User user = userBuilder.buildUser();

        prepareTest(RandomStringUtils.randomAlphabetic(20), user);

        ExpenseCategoryCreateOrUpdateRequest request = ExpenseCategoryBuilder.buildRequest(
                RandomStringUtils.randomAlphabetic(20));

        mockMvc.perform(post("/api/v1/expense-category")
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

        ExpenseCategoryCreateOrUpdateRequest request = ExpenseCategoryBuilder.buildRequest(randomString);

        mockMvc.perform(post("/api/v1/expense-category")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", tokenHelper.getToken(user.getEmail())))
                .andExpect(status().isConflict());
    }

    @Test
    @Transactional
    void delete_successful() throws Exception {
        User user = userBuilder.buildUser();

        ExpenseCategory expenseCategory = prepareTest(RandomStringUtils.randomAlphabetic(20), user);

        mockMvc.perform(delete("/api/v1/expense-category/" + expenseCategory.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", tokenHelper.getToken(user.getEmail())))
                .andExpect(status().isNoContent());
    }

    @Test
    @Transactional
    void delete_unsuccessful() throws Exception {
        User user = userBuilder.buildUser();

        mockMvc.perform(delete(String.format("/api/v1/expense-category/%s", UUID.randomUUID()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", tokenHelper.getToken(user.getEmail())))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void update_successful() throws Exception {
        User user = userBuilder.buildUser();

        ExpenseCategory expenseCategory = prepareTest(RandomStringUtils.randomAlphabetic(20), user);

        ExpenseCategoryCreateOrUpdateRequest request = ExpenseCategoryBuilder.buildRequest(
                RandomStringUtils.randomAlphabetic(20));

        mockMvc.perform(put(String.format("/api/v1/expense-category/%s/edit", expenseCategory.getId()))
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
        ExpenseCategory expenseCategory = prepareTest(randomString, user);

        ExpenseCategoryCreateOrUpdateRequest request = ExpenseCategoryBuilder.buildRequest(randomString);

        mockMvc.perform(put(String.format("/api/v1/expense-category/%s/edit", expenseCategory.getId()))
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", tokenHelper.getToken(user.getEmail())))
                .andExpect(status().isConflict());
    }

    @Test
    @Transactional
    void find_successful() throws Exception {
        User user = userBuilder.buildUser();

        ExpenseCategory expenseCategory = prepareTest(RandomStringUtils.randomAlphabetic(20), user);

        mockMvc.perform(get(String.format("/api/v1/expense-category/%s", expenseCategory.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", tokenHelper.getToken(user.getEmail())))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    void find_unsuccessful() throws Exception {
        User user = userBuilder.buildUser();

        mockMvc.perform(get(String.format("/api/v1/expense-category/%s", UUID.randomUUID()))
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

        mockMvc.perform(get("/api/v1/expense-category")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", tokenHelper.getToken(user.getEmail())))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    void find_empty_successful() throws Exception {
        User user = userBuilder.buildUser();

        mockMvc.perform(get("/api/v1/expense-category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", tokenHelper.getToken(user.getEmail())))
                .andExpect(status().isOk());
    }

    private ExpenseCategory prepareTest(String name, User user) {
        return builder.buildEntity(name, user.getId(), null);
    }
}