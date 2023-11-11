package de.whiteo.mylfa.builder;

import de.whiteo.mylfa.domain.IncomeCategory;
import de.whiteo.mylfa.dto.incomecategory.IncomeCategoryCreateOrUpdateRequest;
import de.whiteo.mylfa.repository.IncomeCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;
import java.util.UUID;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@RequiredArgsConstructor
public class IncomeCategoryBuilder {

    private final static Random random = new Random();

    private final IncomeCategoryRepository repository;

    public static IncomeCategoryCreateOrUpdateRequest buildRequest(String name) {
        IncomeCategoryCreateOrUpdateRequest request = new IncomeCategoryCreateOrUpdateRequest();
        request.setName(name);
        request.setHide(new Random().nextBoolean());
        return request;
    }

    public static IncomeCategoryCreateOrUpdateRequest buildRequestWithParentId(String name, UUID parentId) {
        IncomeCategoryCreateOrUpdateRequest request = new IncomeCategoryCreateOrUpdateRequest();
        request.setName(name);
        request.setHide(new Random().nextBoolean());
        request.setParentId(parentId);
        return request;
    }

    public static IncomeCategory buildIncomeCategory(UUID parentId) {
        IncomeCategory incomeCategory = new IncomeCategory();
        incomeCategory.setId(UUID.randomUUID());
        incomeCategory.setParentId(parentId);
        incomeCategory.setHide(random.nextBoolean());
        incomeCategory.setName(RandomStringUtils.randomAlphabetic(20));
        return incomeCategory;
    }

    public IncomeCategory buildEntity(String name, UUID userId, UUID parentId) {
        IncomeCategory incomeCategory = new IncomeCategory();
        incomeCategory.setHide(new Random().nextBoolean());
        incomeCategory.setName(name);
        incomeCategory.setParentId(parentId);
        incomeCategory.setUserId(userId);
        return repository.save(incomeCategory);
    }
}