package de.whiteo.mylfa.mapper;

import de.whiteo.mylfa.builder.IncomeCategoryBuilder;
import de.whiteo.mylfa.domain.IncomeCategory;
import de.whiteo.mylfa.dto.incomecategory.IncomeCategoryResponse;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

class IncomeCategoryMapperTest {

    private final IncomeCategoryMapper mapper = Mappers.getMapper(IncomeCategoryMapper.class);

    @Test
    void testToResponse() {
        IncomeCategory parentCategory = IncomeCategoryBuilder.buildIncomeCategory(null);
        IncomeCategory category = IncomeCategoryBuilder.buildIncomeCategory(parentCategory.getId());
        IncomeCategoryResponse response = mapper.toResponse(category, mapper.toResponse(parentCategory));
        assertEquals(category.getId(), response.getId());
        assertEquals(category.getParentId(), response.getParent().getId());
        assertEquals(category.getName(), response.getName());
        assertEquals(category.isHide(), response.isHide());
    }
}