package de.whiteo.mylfa.repository;

import de.whiteo.mylfa.domain.IncomeCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

public interface IncomeCategoryRepository extends AbstractRepository<IncomeCategory> {

    Optional<IncomeCategory> findByNameIgnoreCase(String name);

    @Query("SELECT ic, p FROM IncomeCategory ic " +
            "LEFT JOIN IncomeCategory p ON ic.parentId = p.id AND ic.userId = :userId " +
            "WHERE ic.userId = :userId " +
            "AND ic.id = :id")
    List<Object[]> findByUserIdAndId(@Param("userId") UUID userId, @Param("id") UUID id);

    @Query("SELECT ic, p FROM IncomeCategory ic " +
            "LEFT JOIN ExpenseCategory p ON ic.parentId = p.id " +
            "WHERE ic.userId = :userId " +
            "AND (:hide IS NULL OR ic.hide = :hide)")
    Page<Object[]> findAllByParamsWithJoin(@Param("userId") UUID userId, @Param("hide") Boolean hide, Pageable pageable);
}