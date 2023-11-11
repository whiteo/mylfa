package de.whiteo.mylfa.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.whiteo.mylfa.builder.CurrencyTypeBuilder;
import de.whiteo.mylfa.builder.IncomeBuilder;
import de.whiteo.mylfa.builder.IncomeCategoryBuilder;
import de.whiteo.mylfa.builder.UserBuilder;
import de.whiteo.mylfa.domain.CurrencyType;
import de.whiteo.mylfa.domain.Income;
import de.whiteo.mylfa.domain.IncomeCategory;
import de.whiteo.mylfa.domain.User;
import de.whiteo.mylfa.dto.income.IncomeCreateOrUpdateRequest;
import de.whiteo.mylfa.helper.TokenHelper;
import de.whiteo.mylfa.repository.CurrencyTypeRepository;
import de.whiteo.mylfa.repository.IncomeCategoryRepository;
import de.whiteo.mylfa.repository.IncomeRepository;
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
class IncomeRestControllerTest {

    @Autowired
    private IncomeCategoryRepository incomeCategoryRepository;
    @Autowired
    private CurrencyTypeRepository currencyTypeRepository;
    @Autowired
    private WebApplicationContext webApplicationContext;
    private IncomeCategoryBuilder incomeCategoryBuilder;
    private CurrencyTypeBuilder currencyTypeBuilder;
    @Autowired
    private IncomeRepository repository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    private TokenHelper tokenHelper;
    private UserBuilder userBuilder;
    private IncomeBuilder builder;
    private MockMvc mockMvc;

    @PostConstruct
    void initialize() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        incomeCategoryBuilder = new IncomeCategoryBuilder(incomeCategoryRepository);
        currencyTypeBuilder = new CurrencyTypeBuilder(currencyTypeRepository);
        userBuilder = new UserBuilder(userRepository);
        tokenHelper = new TokenHelper(jwtTokenUtil);
        builder = new IncomeBuilder(repository);
    }

    @Test
    @Transactional
    void create_successful() throws Exception {
        User user = userBuilder.buildUser();

        Income income = prepareTest(RandomStringUtils.randomAlphabetic(20), user.getId());

        IncomeCreateOrUpdateRequest request = IncomeBuilder.buildRequest(
                income.getCategoryId(), income.getCurrencyTypeId());

        mockMvc.perform(post("/api/v1/income")
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
        Income income = prepareTest(randomString, user.getId());

        IncomeCreateOrUpdateRequest request = IncomeBuilder.buildRequest(
                income.getCategoryId(), UUID.randomUUID());

        mockMvc.perform(post("/api/v1/income")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", tokenHelper.getToken(user.getEmail())))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void delete_successful() throws Exception {
        User user = userBuilder.buildUser();

        Income income = prepareTest(RandomStringUtils.randomAlphabetic(20), user.getId());

        mockMvc.perform(delete("/api/v1/income/" + income.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", tokenHelper.getToken(user.getEmail())))
                .andExpect(status().isNoContent());
    }

    @Test
    @Transactional
    void delete_unsuccessful() throws Exception {
        User user = userBuilder.buildUser();

        mockMvc.perform(delete(String.format("/api/v1/income/%s", UUID.randomUUID()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", tokenHelper.getToken(user.getEmail())))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void update_successful() throws Exception {
        User user = userBuilder.buildUser();

        Income income = prepareTest(RandomStringUtils.randomAlphabetic(20), user.getId());

        IncomeCreateOrUpdateRequest request = IncomeBuilder.buildRequest(
                income.getCategoryId(), income.getCurrencyTypeId());

        mockMvc.perform(put(String.format("/api/v1/income/%s/edit", income.getId()))
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
        Income income = prepareTest(randomString, user.getId());

        IncomeCreateOrUpdateRequest request = IncomeBuilder.buildRequest(
                income.getCategoryId(), income.getCurrencyTypeId());


        mockMvc.perform(put(String.format("/api/v1/income/%s/edit", UUID.randomUUID()))
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", tokenHelper.getToken(user.getEmail())))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void find_successful() throws Exception {
        User user = userBuilder.buildUser();

        Income income = prepareTest(RandomStringUtils.randomAlphabetic(20), user.getId());

        mockMvc.perform(get(String.format("/api/v1/income/%s", income.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", tokenHelper.getToken(user.getEmail())))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    void find_unsuccessful() throws Exception {
        User user = userBuilder.buildUser();

        mockMvc.perform(get(String.format("/api/v1/income/%s", UUID.randomUUID()))
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

        mockMvc.perform(get("/api/v1/income")
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

        mockMvc.perform(get("/api/v1/income")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", tokenHelper.getToken(user.getEmail())))
                .andExpect(status().isOk());
    }

    private Income prepareTest(String name, UUID userId) {
        CurrencyType currencyType = currencyTypeBuilder.buildEntity(name, userId);

        IncomeCategory incomeCategory = incomeCategoryBuilder.buildEntity(name, userId, null);

        return builder.buildEntity(userId, incomeCategory.getId(), currencyType.getId());
    }
}