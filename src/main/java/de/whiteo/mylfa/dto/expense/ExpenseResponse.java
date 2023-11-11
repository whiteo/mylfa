package de.whiteo.mylfa.dto.expense;

import de.whiteo.mylfa.dto.currencytype.CurrencyTypeResponse;
import de.whiteo.mylfa.dto.expensecategory.ExpenseCategoryResponse;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@Data
@Builder
public class ExpenseResponse {

    private UUID id;
    private ExpenseCategoryResponse category;
    private BigDecimal amount;
    private String description;
    private CurrencyTypeResponse currencyType;
}