package de.whiteo.mylfa.builder;

import de.whiteo.mylfa.domain.Expense;
import de.whiteo.mylfa.dto.expense.ExpenseCreateOrUpdateRequest;
import de.whiteo.mylfa.dto.expense.ExpenseFindAllRequest;
import de.whiteo.mylfa.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;
import java.util.Random;
import java.util.UUID;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@RequiredArgsConstructor
public class ExpenseBuilder {

    private final static Random random = new Random();
    private final ExpenseRepository repository;

    public static ExpenseCreateOrUpdateRequest buildRequest(UUID incomeCategoryId, UUID currencyTypeId) {
        ExpenseCreateOrUpdateRequest request = new ExpenseCreateOrUpdateRequest();
        request.setDescription(RandomStringUtils.randomAlphabetic(20));
        request.setAmount(BigDecimal.valueOf(new Random().nextInt(100)));
        request.setCategoryId(incomeCategoryId);
        request.setCurrencyTypeId(currencyTypeId);
        return request;
    }

    public static ExpenseFindAllRequest buildFindAllRequest() {
        return new ExpenseFindAllRequest();
    }

    public static Expense buildExpense(UUID currencyTypeId, UUID categoryId) {
        Expense expense = new Expense();
        expense.setId(UUID.randomUUID());
        expense.setUserId(UUID.randomUUID());
        expense.setAmount(BigDecimal.valueOf(random.nextInt()));
        expense.setCategoryId(categoryId);
        expense.setCurrencyTypeId(currencyTypeId);
        expense.setDescription(RandomStringUtils.randomAlphabetic(20));
        return expense;
    }

    public Expense buildEntity(UUID userId, UUID outlayCategoryId, UUID CurrencyTypeId) {
        Expense expense = new Expense();
        expense.setAmount(BigDecimal.valueOf(new Random().nextInt()));
        expense.setCategoryId(outlayCategoryId);
        expense.setDescription(RandomStringUtils.randomAlphabetic(20));
        expense.setUserId(userId);
        expense.setCurrencyTypeId(CurrencyTypeId);
        return repository.save(expense);
    }
}