package de.whiteo.mylfa.mapper;

import de.whiteo.mylfa.builder.CurrencyTypeBuilder;
import de.whiteo.mylfa.builder.IncomeBuilder;
import de.whiteo.mylfa.builder.IncomeCategoryBuilder;
import de.whiteo.mylfa.domain.Income;
import de.whiteo.mylfa.dto.currencytype.CurrencyTypeResponse;
import de.whiteo.mylfa.dto.income.IncomeResponse;
import de.whiteo.mylfa.dto.incomecategory.IncomeCategoryResponse;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

class IncomeMapperTest {

    private final CurrencyTypeMapper currencyTypeMapper = Mappers.getMapper(CurrencyTypeMapper.class);
    private final IncomeCategoryMapper categoryMapper = Mappers.getMapper(IncomeCategoryMapper.class);
    private final IncomeMapper mapper = Mappers.getMapper(IncomeMapper.class);

    @Test
    void testToResponse() {
        CurrencyTypeResponse currencyTypeResponse = currencyTypeMapper.toResponse(
                CurrencyTypeBuilder.buildCurrencyType());

        IncomeCategoryResponse categoryResponse = categoryMapper.toResponse(
                IncomeCategoryBuilder.buildIncomeCategory(null));

        Income income = IncomeBuilder.buildIncome(currencyTypeResponse.getId(), categoryResponse.getId());

        IncomeResponse response = mapper.toResponse(income, currencyTypeResponse, categoryResponse);
        assertEquals(income.getId(), response.getId());
        assertEquals(income.getDescription(), response.getDescription());
        assertEquals(income.getAmount(), response.getAmount());
        assertEquals(income.getCategoryId(), response.getCategory().getId());
        assertEquals(income.getCurrencyTypeId(), response.getCurrencyType().getId());
    }
}