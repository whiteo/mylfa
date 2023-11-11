package de.whiteo.mylfa.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Expense extends AbstractEntity {

    @Column(name = "category_id", nullable = false, columnDefinition = "UUID")
    private UUID categoryId;

    @Column(name = "currency_type_id", nullable = false, columnDefinition = "UUID")
    private UUID currencyTypeId;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "description")
    private String description;
}