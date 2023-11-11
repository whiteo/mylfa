package de.whiteo.mylfa.dto.incomecategory;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@Data
@Builder
public class IncomeCategoryResponse {

    private UUID id;
    private String name;
    private boolean hide;
    private IncomeCategoryResponse parent;
}