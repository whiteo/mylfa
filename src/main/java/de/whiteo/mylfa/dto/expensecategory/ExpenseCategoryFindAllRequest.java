package de.whiteo.mylfa.dto.expensecategory;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@Data
public class ExpenseCategoryFindAllRequest {

    @NotNull
    private Boolean hide;
}