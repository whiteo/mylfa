package de.whiteo.mylfa.mapper;

import de.whiteo.mylfa.builder.ExpenseCategoryBuilder;
import de.whiteo.mylfa.domain.ExpenseCategory;
import de.whiteo.mylfa.dto.expensecategory.ExpenseCategoryResponse;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

class ExpenseCategoryMapperTest {

    private final ExpenseCategoryMapper mapper = Mappers.getMapper(ExpenseCategoryMapper.class);

    @Test
    void testToResponse() {
        ExpenseCategory parentCategory = ExpenseCategoryBuilder.buildExpenseCategory(null);
        ExpenseCategory category = ExpenseCategoryBuilder.buildExpenseCategory(parentCategory.getId());
        ExpenseCategoryResponse response = mapper.toResponse(category, mapper.toResponse(parentCategory));
        assertEquals(category.getId(), response.getId());
        assertEquals(category.getParentId(), response.getParent().getId());
        assertEquals(category.getName(), response.getName());
        assertEquals(category.isHide(), response.isHide());
    }
}