package de.whiteo.mylfa.dto.income;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@Data
public class IncomeCreateOrUpdateRequest {

    @NotNull(message = "Currency type id cannot be null")
    private UUID currencyTypeId;

    @NotNull(message = "Category id cannot be null")
    private UUID categoryId;

    @NotNull(message = "Amount cannot be null")
    @Positive
    private BigDecimal amount;

    private String description;
}