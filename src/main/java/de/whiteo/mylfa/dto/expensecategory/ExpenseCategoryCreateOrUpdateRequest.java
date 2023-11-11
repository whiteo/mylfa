package de.whiteo.mylfa.dto.expensecategory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@Data
public class ExpenseCategoryCreateOrUpdateRequest {

    @NotNull(message = "Name cannot be null")
    @NotBlank(message = "Name cannot be blank")
    @Size(min = 1, max = 30, message = "Name must be between 1 and 30 characters")
    private String name;

    private boolean hide;

    private UUID parentId;
}