package de.whiteo.mylfa.mapper;

import de.whiteo.mylfa.domain.CurrencyType;
import de.whiteo.mylfa.dto.currencytype.CurrencyTypeResponse;
import org.mapstruct.Mapper;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@Mapper(componentModel = "spring")
public interface CurrencyTypeMapper extends AbstractMapper<CurrencyType, CurrencyTypeResponse> {

    @Override
    CurrencyTypeResponse toResponse(CurrencyType entity);
}