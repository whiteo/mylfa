package de.whiteo.mylfa.repository;

import de.whiteo.mylfa.domain.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

public interface ExpenseRepository extends AbstractRepository<Expense> {

    @Query("SELECT e, ct, ec FROM Expense e " +
            "LEFT JOIN CurrencyType ct ON e.currencyTypeId = ct.id AND ct.userId = :userId " +
            "LEFT JOIN ExpenseCategory ec ON e.categoryId = ec.id AND ec.userId = :userId " +
            "WHERE e.userId = :userId " +
            "AND e.id = :id")
    List<Object[]> findByUserIdAndId(@Param("userId") UUID userId, @Param("id") UUID id);

    @Query("SELECT e, ct, ec FROM Expense e " +
            "LEFT JOIN CurrencyType ct ON e.currencyTypeId = ct.id AND ct.userId = :userId " +
            "LEFT JOIN ExpenseCategory ec ON e.categoryId = ec.id AND ec.userId = :userId " +
            "WHERE e.userId = :userId " +
            "AND (coalesce(:categoryId, NULL) IS NULL OR e.categoryId = :categoryId) " +
            "AND (coalesce(:currencyTypeId, NULL) IS NULL OR e.currencyTypeId = :currencyTypeId) " +
            "AND (coalesce(:startDate, NULL) IS NULL OR e.creationDate >= :startDate) " +
            "AND (coalesce(:endDate, NULL) IS NULL OR e.creationDate <= :endDate)")
    Page<Object[]> findAllByParamsWithJoin(@Param("userId") UUID userId,
                                           @Param("categoryId") UUID categoryId,
                                           @Param("currencyTypeId") UUID currencyTypeId,
                                           @Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate,
                                           Pageable pageable);
}