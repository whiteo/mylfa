package de.whiteo.mylfa.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.whiteo.mylfa.builder.UserBuilder;
import de.whiteo.mylfa.domain.User;
import de.whiteo.mylfa.dto.user.UserLoginRequest;
import de.whiteo.mylfa.repository.UserRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AuthenticateRestControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;
    private UserBuilder userBuilder;
    private MockMvc mockMvc;

    @PostConstruct
    void initialize() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        userBuilder = new UserBuilder(userRepository);
    }

    @Test
    @Transactional
    void authenticate_successful() throws Exception {
        User user = userBuilder.buildUser();

        UserLoginRequest request = prepareTest(user.getEmail(), String.valueOf(user.getPassword()));

        mockMvc.perform(post("/api/v1/authenticate")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    void authenticate_unsuccessful() throws Exception {
        UserLoginRequest request = prepareTest(RandomStringUtils.randomAlphabetic(5) + "@test.com",
                RandomStringUtils.randomAlphabetic(20));

        mockMvc.perform(post("/api/v1/authenticate")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    private UserLoginRequest prepareTest(String email, String passwd) {
        return UserBuilder.buildUserLoginRequest(email, passwd);
    }
}