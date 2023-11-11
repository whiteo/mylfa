package de.whiteo.mylfa.mapper;

import de.whiteo.mylfa.domain.ExpenseCategory;
import de.whiteo.mylfa.dto.expensecategory.ExpenseCategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@Mapper(componentModel = "spring")
public interface ExpenseCategoryMapper extends AbstractMapper<ExpenseCategory, ExpenseCategoryResponse> {

    @Override
    ExpenseCategoryResponse toResponse(ExpenseCategory entity);

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "name", source = "entity.name")
    @Mapping(target = "hide", source = "entity.hide")
    @Mapping(target = "parent", source = "parent")
    ExpenseCategoryResponse toResponse(ExpenseCategory entity, ExpenseCategoryResponse parent);
}