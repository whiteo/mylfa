package de.whiteo.mylfa.dto.currencytype;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@Data
public class CurrencyTypeFindAllRequest {

    @NotNull
    private Boolean hide;
}