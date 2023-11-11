package de.whiteo.mylfa.mapper;

import de.whiteo.mylfa.builder.CurrencyTypeBuilder;
import de.whiteo.mylfa.builder.ExpenseBuilder;
import de.whiteo.mylfa.builder.ExpenseCategoryBuilder;
import de.whiteo.mylfa.domain.Expense;
import de.whiteo.mylfa.dto.currencytype.CurrencyTypeResponse;
import de.whiteo.mylfa.dto.expense.ExpenseResponse;
import de.whiteo.mylfa.dto.expensecategory.ExpenseCategoryResponse;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

class ExpenseMapperTest {

    private final ExpenseCategoryMapper categoryMapper = Mappers.getMapper(ExpenseCategoryMapper.class);
    private final CurrencyTypeMapper currencyTypeMapper = Mappers.getMapper(CurrencyTypeMapper.class);
    private final ExpenseMapper mapper = Mappers.getMapper(ExpenseMapper.class);

    @Test
    void testToResponse() {
        CurrencyTypeResponse currencyTypeResponse = currencyTypeMapper.toResponse(
                CurrencyTypeBuilder.buildCurrencyType());

        ExpenseCategoryResponse categoryResponse = categoryMapper.toResponse(
                ExpenseCategoryBuilder.buildExpenseCategory(null));

        Expense expense = ExpenseBuilder.buildExpense(currencyTypeResponse.getId(), categoryResponse.getId());

        ExpenseResponse response = mapper.toResponse(expense, currencyTypeResponse, categoryResponse);
        assertEquals(expense.getId(), response.getId());
        assertEquals(expense.getDescription(), response.getDescription());
        assertEquals(expense.getAmount(), response.getAmount());
        assertEquals(expense.getCategoryId(), response.getCategory().getId());
        assertEquals(expense.getCurrencyTypeId(), response.getCurrencyType().getId());
    }
}