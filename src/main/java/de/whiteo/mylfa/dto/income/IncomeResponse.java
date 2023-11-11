package de.whiteo.mylfa.dto.income;

import de.whiteo.mylfa.dto.currencytype.CurrencyTypeResponse;
import de.whiteo.mylfa.dto.incomecategory.IncomeCategoryResponse;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@Data
@Builder
public class IncomeResponse {

    private UUID id;
    private IncomeCategoryResponse category;
    private BigDecimal amount;
    private String description;
    private CurrencyTypeResponse currencyType;
}