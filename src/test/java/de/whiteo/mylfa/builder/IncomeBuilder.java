package de.whiteo.mylfa.builder;

import de.whiteo.mylfa.domain.Income;
import de.whiteo.mylfa.dto.income.IncomeCreateOrUpdateRequest;
import de.whiteo.mylfa.dto.income.IncomeFindAllRequest;
import de.whiteo.mylfa.repository.IncomeRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;
import java.util.Random;
import java.util.UUID;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@RequiredArgsConstructor
public class IncomeBuilder {
    private final static Random random = new Random();
    private final IncomeRepository repository;

    public static IncomeCreateOrUpdateRequest buildRequest(UUID incomeCategoryId, UUID currencyTypeId) {
        IncomeCreateOrUpdateRequest request = new IncomeCreateOrUpdateRequest();
        request.setDescription(RandomStringUtils.randomAlphabetic(20));
        request.setAmount(BigDecimal.valueOf(new Random().nextInt(100)));
        request.setCategoryId(incomeCategoryId);
        request.setCurrencyTypeId(currencyTypeId);
        return request;
    }

    public static IncomeFindAllRequest buildFindAllRequest() {
        return new IncomeFindAllRequest();
    }

    public static Income buildIncome(UUID currencyTypeId, UUID categoryId) {
        Income income = new Income();
        income.setId(UUID.randomUUID());
        income.setUserId(UUID.randomUUID());
        income.setAmount(BigDecimal.valueOf(random.nextInt()));
        income.setCategoryId(categoryId);
        income.setCurrencyTypeId(currencyTypeId);
        income.setDescription(RandomStringUtils.randomAlphabetic(20));
        return income;
    }

    public Income buildEntity(UUID userId, UUID incomeCategoryId, UUID CurrencyTypeId) {
        Income income = new Income();
        income.setAmount(BigDecimal.valueOf(new Random().nextInt()));
        income.setCategoryId(incomeCategoryId);
        income.setDescription(RandomStringUtils.randomAlphabetic(20));
        income.setUserId(userId);
        income.setCurrencyTypeId(CurrencyTypeId);
        return repository.save(income);
    }
}