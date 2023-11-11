package de.whiteo.mylfa.mapper;

import de.whiteo.mylfa.domain.Expense;
import de.whiteo.mylfa.dto.currencytype.CurrencyTypeResponse;
import de.whiteo.mylfa.dto.expense.ExpenseResponse;
import de.whiteo.mylfa.dto.expensecategory.ExpenseCategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@Mapper(componentModel = "spring")
public interface ExpenseMapper extends AbstractMapper<Expense, ExpenseResponse> {

    @Override
    ExpenseResponse toResponse(Expense entity);

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "amount", source = "entity.amount")
    @Mapping(target = "description", source = "entity.description")
    @Mapping(target = "currencyType", source = "currencyType")
    @Mapping(target = "category", source = "category")
    ExpenseResponse toResponse(Expense entity, CurrencyTypeResponse currencyType, ExpenseCategoryResponse category);
}