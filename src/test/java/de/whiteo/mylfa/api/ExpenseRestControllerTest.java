package de.whiteo.mylfa.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.whiteo.mylfa.builder.CurrencyTypeBuilder;
import de.whiteo.mylfa.builder.ExpenseBuilder;
import de.whiteo.mylfa.builder.ExpenseCategoryBuilder;
import de.whiteo.mylfa.builder.UserBuilder;
import de.whiteo.mylfa.domain.CurrencyType;
import de.whiteo.mylfa.domain.Expense;
import de.whiteo.mylfa.domain.ExpenseCategory;
import de.whiteo.mylfa.domain.User;
import de.whiteo.mylfa.dto.expense.ExpenseCreateOrUpdateRequest;
import de.whiteo.mylfa.helper.TokenHelper;
import de.whiteo.mylfa.repository.CurrencyTypeRepository;
import de.whiteo.mylfa.repository.ExpenseCategoryRepository;
import de.whiteo.mylfa.repository.ExpenseRepository;
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
class ExpenseRestControllerTest {

    @Autowired
    private ExpenseCategoryRepository expenseCategoryRepository;
    @Autowired
    private CurrencyTypeRepository currencyTypeRepository;
    @Autowired
    private WebApplicationContext webApplicationContext;
    private ExpenseCategoryBuilder expenseCategoryBuilder;
    private CurrencyTypeBuilder currencyTypeBuilder;
    @Autowired
    private ExpenseRepository repository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    private TokenHelper tokenHelper;
    private UserBuilder userBuilder;
    private ExpenseBuilder builder;
    private MockMvc mockMvc;

    @PostConstruct
    void initialize() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        expenseCategoryBuilder = new ExpenseCategoryBuilder(expenseCategoryRepository);
        currencyTypeBuilder = new CurrencyTypeBuilder(currencyTypeRepository);
        userBuilder = new UserBuilder(userRepository);
        tokenHelper = new TokenHelper(jwtTokenUtil);
        builder = new ExpenseBuilder(repository);
    }

    @Test
    @Transactional
    void create_successful() throws Exception {
        User user = userBuilder.buildUser();

        Expense expense = prepareTest(RandomStringUtils.randomAlphabetic(20), user.getId());

        ExpenseCreateOrUpdateRequest request = ExpenseBuilder.buildRequest(
                expense.getCategoryId(), expense.getCurrencyTypeId());

        mockMvc.perform(post("/api/v1/expense")
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
        Expense expense = prepareTest(randomString, user.getId());

        ExpenseCreateOrUpdateRequest request = ExpenseBuilder.buildRequest(
                expense.getCategoryId(), UUID.randomUUID());

        mockMvc.perform(post("/api/v1/expense")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", tokenHelper.getToken(user.getEmail())))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void delete_successful() throws Exception {
        User user = userBuilder.buildUser();

        Expense expense = prepareTest(RandomStringUtils.randomAlphabetic(20), user.getId());

        mockMvc.perform(delete("/api/v1/expense/" + expense.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", tokenHelper.getToken(user.getEmail())))
                .andExpect(status().isNoContent());
    }

    @Test
    @Transactional
    void delete_unsuccessful() throws Exception {
        User user = userBuilder.buildUser();

        mockMvc.perform(delete(String.format("/api/v1/expense/%s", UUID.randomUUID()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", tokenHelper.getToken(user.getEmail())))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void update_successful() throws Exception {
        User user = userBuilder.buildUser();

        Expense expense = prepareTest(RandomStringUtils.randomAlphabetic(20), user.getId());

        ExpenseCreateOrUpdateRequest request = ExpenseBuilder.buildRequest(
                expense.getCategoryId(), expense.getCurrencyTypeId());

        mockMvc.perform(put(String.format("/api/v1/expense/%s/edit", expense.getId()))
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
        Expense expense = prepareTest(randomString, user.getId());

        ExpenseCreateOrUpdateRequest request = ExpenseBuilder.buildRequest(
                expense.getCategoryId(), expense.getCurrencyTypeId());


        mockMvc.perform(put(String.format("/api/v1/expense/%s/edit", UUID.randomUUID()))
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", tokenHelper.getToken(user.getEmail())))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void find_successful() throws Exception {
        User user = userBuilder.buildUser();

        Expense expense = prepareTest(RandomStringUtils.randomAlphabetic(20), user.getId());

        mockMvc.perform(get(String.format("/api/v1/expense/%s", expense.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", tokenHelper.getToken(user.getEmail())))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    void find_unsuccessful() throws Exception {
        User user = userBuilder.buildUser();

        mockMvc.perform(get(String.format("/api/v1/expense/%s", UUID.randomUUID()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", tokenHelper.getToken(user.getEmail())))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void find_all_successful() throws Exception {
        User user = userBuilder.buildUser();

        for (int i = 0; i < 10; i++) {
            prepareTest(RandomStringUtils.randomAlphabetic(20), user.getId());
        }

        mockMvc.perform(get("/api/v1/expense")
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

        mockMvc.perform(get("/api/v1/expense")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", tokenHelper.getToken(user.getEmail())))
                .andExpect(status().isOk());
    }

    private Expense prepareTest(String name, UUID userId) {
        CurrencyType currencyType = currencyTypeBuilder.buildEntity(name, userId);

        ExpenseCategory expenseCategory = expenseCategoryBuilder.buildEntity(name, userId, null);

        return builder.buildEntity(userId, expenseCategory.getId(), currencyType.getId());
    }
}