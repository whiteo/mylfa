package de.whiteo.mylfa.service;

import de.whiteo.mylfa.aspect.NoModifyDemoMode;
import de.whiteo.mylfa.domain.User;
import de.whiteo.mylfa.dto.user.UserCreateRequest;
import de.whiteo.mylfa.dto.user.UserLoginRequest;
import de.whiteo.mylfa.dto.user.UserLoginResponse;
import de.whiteo.mylfa.dto.user.UserResponse;
import de.whiteo.mylfa.dto.user.UserUpdatePasswordRequest;
import de.whiteo.mylfa.dto.user.UserUpdatePropertiesRequest;
import de.whiteo.mylfa.dto.user.UserUpdateRequest;
import de.whiteo.mylfa.exception.ExecutionConflictException;
import de.whiteo.mylfa.mapper.UserMapper;
import de.whiteo.mylfa.repository.CurrencyTypeRepository;
import de.whiteo.mylfa.repository.ExpenseCategoryRepository;
import de.whiteo.mylfa.repository.ExpenseRepository;
import de.whiteo.mylfa.repository.IncomeCategoryRepository;
import de.whiteo.mylfa.repository.IncomeRepository;
import de.whiteo.mylfa.repository.UserRepository;
import de.whiteo.mylfa.security.TokenInteract;
import de.whiteo.mylfa.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.UUID;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final IncomeCategoryRepository incomeCategoryRepository;
    private final ExpenseCategoryRepository expenseCategoryRepository;
    private final CurrencyTypeRepository currencyTypeRepository;
    private final ExpenseRepository expenseRepository;
    private final IncomeRepository incomeRepository;
    private final LanguageService languageService;
    private final TokenInteract tokenInteract;
    private final UserRepository repository;
    private final UserMapper mapper;

    public UserResponse find(UUID id) {
        return mapper.toResponse(repository.getOrThrow(id));
    }

    @Transactional
    @NoModifyDemoMode
    public void delete(UUID id) {
        User user = repository.getOrThrow(id);

        incomeCategoryRepository.deleteAllByUserId(user.getId());
        incomeRepository.deleteAllByUserId(user.getId());

        expenseCategoryRepository.deleteAllByUserId(user.getId());
        expenseRepository.deleteAllByUserId(user.getId());

        currencyTypeRepository.deleteAllByUserId(user.getId());

        repository.delete(user);
    }

    @NoModifyDemoMode
    public UserResponse create(UserCreateRequest request) {
        checkUniqueEmail(request.getEmail());

        String lang = request.getProperties().get("lang");
        languageService.download(lang);

        User user = new User();
        user.setEmail(request.getEmail());
        user.setVerified(false);
        user.setPassword(request.getPassword().toCharArray());
        user.setProperties(request.getProperties());
        return mapper.toResponse(repository.save(user));
    }

    @NoModifyDemoMode
    public UserResponse update(UUID id, UserUpdateRequest request) {
        User user = repository.getOrThrow(id);

        checkUniqueEmail(request.getEmail());

        user.setVerified(false);
        user.setEmail(request.getEmail());
        return mapper.toResponse(repository.save(user));
    }

    @NoModifyDemoMode
    public UserResponse updateProperties(UUID id, UserUpdatePropertiesRequest updateRequest) {
        if (!updateRequest.getProperties().containsKey("lang")) {
            throw new ExecutionConflictException("Lang property not found");
        }

        User user = repository.getOrThrow(id);
        user.setProperties(updateRequest.getProperties());
        return mapper.toResponse(repository.save(user));
    }

    @NoModifyDemoMode
    public UserResponse updatePassword(UUID id, UserUpdatePasswordRequest request) {
        User user = repository.getOrThrow(id);

        passwdVerification(user.getPassword(), request.getOldPassword().toCharArray());

        user.setPassword(request.getPassword().toCharArray());
        return mapper.toResponse(repository.save(user));
    }

    public String getLangProperty(User user) {
        return user.getProperties().get("lang");
    }

    public UserLoginResponse getToken(UUID id, UserLoginRequest request) {
        User user = null == id ? findByEmail(request.getEmail()) : repository.getOrThrow(id);

        passwdVerification(user.getPassword(), request.getPassword().toCharArray());

        UserResponse response = mapper.toResponse(user);

        return mapper.toLoginResponse(response, tokenInteract.generateToken(loadUserByUsername(user.getEmail())));
    }

    public Boolean validateToken(HttpServletRequest request) {
        String token = tokenInteract.getToken(request);
        return tokenInteract.validateToken(token);
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = findByEmail(name);
        return UserDetailsImpl.builder()
                .id(user.getId())
                .name(user.getEmail())
                .password(user.getPassword()).build();
    }

    public User findByEmail(String email) {
        return repository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new BadCredentialsException("User not found with email: " + email));
    }

    private void passwdVerification(char[] passwd1, char[] passwd2) {
        if (!Arrays.equals(passwd1, passwd2)) {
            throw new ExecutionConflictException("Password verification failed");
        }
    }

    private void checkUniqueEmail(String email) {
        if (StringUtils.isNotBlank(email)) {
            repository.findByEmailIgnoreCase(email).ifPresent(
                    existingEntity -> {
                        throw new ExecutionConflictException("Object with email '" +
                                email + "' already exists in the system");
                    });
        }
    }
}