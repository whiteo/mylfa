package de.whiteo.mylfa.mapper;

import de.whiteo.mylfa.builder.CurrencyTypeBuilder;
import de.whiteo.mylfa.domain.CurrencyType;
import de.whiteo.mylfa.dto.currencytype.CurrencyTypeResponse;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

class CurrencyTypeMapperTest {

    private final CurrencyTypeMapper mapper = Mappers.getMapper(CurrencyTypeMapper.class);
    
    @Test
    void testToResponse() {
        CurrencyType currencyType = CurrencyTypeBuilder.buildCurrencyType();
        CurrencyTypeResponse response = mapper.toResponse(currencyType);
        assertEquals(currencyType.getId(), response.getId());
        assertEquals(currencyType.getName(), response.getName());
        assertEquals(currencyType.isHide(), response.isHide());
    }
}