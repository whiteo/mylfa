package de.whiteo.mylfa.repository;

import de.whiteo.mylfa.domain.ExpenseCategory;
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

public interface ExpenseCategoryRepository extends AbstractRepository<ExpenseCategory> {

    Optional<ExpenseCategory> findByNameIgnoreCase(String name);

    @Query("SELECT ec, p FROM ExpenseCategory ec " +
            "LEFT JOIN ExpenseCategory p ON ec.parentId = p.id AND ec.userId = :userId " +
            "WHERE ec.userId = :userId " +
            "AND ec.id = :id")
    List<Object[]> findByUserIdAndId(@Param("userId") UUID userId, @Param("id") UUID id);

    @Query("SELECT ec, p FROM ExpenseCategory ec " +
            "LEFT JOIN ExpenseCategory p ON ec.parentId = p.id " +
            "WHERE ec.userId = :userId " +
            "AND (:hide IS NULL OR ec.hide = :hide)")
    Page<Object[]> findAllByParamsWithJoin(@Param("userId") UUID userId, @Param("hide") Boolean hide, Pageable pageable);
}