package de.whiteo.mylfa.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
public class IncomeCategory extends AbstractEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "hide", nullable = false)
    private boolean hide;

    @Column(name = "parent_id")
    private UUID parentId;
}