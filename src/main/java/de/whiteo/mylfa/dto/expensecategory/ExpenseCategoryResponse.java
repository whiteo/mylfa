package de.whiteo.mylfa.dto.expensecategory;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@Data
@Builder
public class ExpenseCategoryResponse {

    private UUID id;
    private String name;
    private boolean hide;
    private ExpenseCategoryResponse parent;
}