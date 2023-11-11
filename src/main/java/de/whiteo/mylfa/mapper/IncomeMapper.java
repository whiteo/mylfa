package de.whiteo.mylfa.mapper;

import de.whiteo.mylfa.domain.Income;
import de.whiteo.mylfa.dto.currencytype.CurrencyTypeResponse;
import de.whiteo.mylfa.dto.income.IncomeResponse;
import de.whiteo.mylfa.dto.incomecategory.IncomeCategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@Mapper(componentModel = "spring")
public interface IncomeMapper extends AbstractMapper<Income, IncomeResponse> {

    @Override
    IncomeResponse toResponse(Income entity);

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "amount", source = "entity.amount")
    @Mapping(target = "description", source = "entity.description")
    @Mapping(target = "currencyType", source = "currencyType")
    @Mapping(target = "category", source = "category")
    IncomeResponse toResponse(Income entity, CurrencyTypeResponse currencyType, IncomeCategoryResponse category);
}