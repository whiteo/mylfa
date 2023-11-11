package de.whiteo.mylfa.builder;

import de.whiteo.mylfa.domain.ExpenseCategory;
import de.whiteo.mylfa.dto.expensecategory.ExpenseCategoryCreateOrUpdateRequest;
import de.whiteo.mylfa.repository.ExpenseCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;
import java.util.UUID;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@RequiredArgsConstructor
public class ExpenseCategoryBuilder {

    private final static Random random = new Random();
    private final ExpenseCategoryRepository repository;

    public static ExpenseCategoryCreateOrUpdateRequest buildRequest(String name) {
        ExpenseCategoryCreateOrUpdateRequest request = new ExpenseCategoryCreateOrUpdateRequest();
        request.setName(name);
        request.setHide(new Random().nextBoolean());
        return request;
    }

    public static ExpenseCategoryCreateOrUpdateRequest buildRequestWithParentId(String name, UUID parentId) {
        ExpenseCategoryCreateOrUpdateRequest request = new ExpenseCategoryCreateOrUpdateRequest();
        request.setName(name);
        request.setHide(new Random().nextBoolean());
        request.setParentId(parentId);
        return request;
    }

    public static ExpenseCategory buildExpenseCategory(UUID parentId) {
        ExpenseCategory category = new ExpenseCategory();
        category.setId(UUID.randomUUID());
        category.setParentId(parentId);
        category.setHide(random.nextBoolean());
        category.setName(RandomStringUtils.randomAlphabetic(20));
        return category;
    }

    public ExpenseCategory buildEntity(String name, UUID userId, UUID parentId) {
        ExpenseCategory expenseCategory = new ExpenseCategory();
        expenseCategory.setHide(new Random().nextBoolean());
        expenseCategory.setName(name);
        expenseCategory.setParentId(parentId);
        expenseCategory.setUserId(userId);
        return repository.save(expenseCategory);
    }
}