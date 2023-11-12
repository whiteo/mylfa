package de.whiteo.mylfa.dto.incomecategory;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@Data
public class IncomeCategoryFindAllRequest {

    @NotNull
    private Boolean hide;
}