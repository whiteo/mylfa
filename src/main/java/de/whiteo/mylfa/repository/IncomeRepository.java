package de.whiteo.mylfa.repository;

import de.whiteo.mylfa.domain.Income;
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

public interface IncomeRepository extends AbstractRepository<Income> {

    @Query("SELECT i, ct, ic FROM Income i " +
            "LEFT JOIN CurrencyType ct ON i.currencyTypeId = ct.id AND ct.userId = :userId " +
            "LEFT JOIN IncomeCategory ic ON i.categoryId = ic.id AND ic.userId = :userId " +
            "WHERE i.userId = :userId " +
            "AND i.id = :id")
    List<Object[]> findByUserIdAndId(@Param("userId") UUID userId, @Param("id") UUID id);

    @Query("SELECT i, ct, ic FROM Income i " +
            "INNER JOIN CurrencyType ct ON i.currencyTypeId = ct.id AND ct.hide = false AND ct.userId = :userId " +
            "INNER JOIN IncomeCategory ic ON i.categoryId = ic.id AND ic.userId = :userId " +
            "WHERE i.userId = :userId " +
            "AND (coalesce(:categoryId, NULL) IS NULL OR i.categoryId = :categoryId) " +
            "AND (coalesce(:currencyTypeId, NULL) IS NULL OR i.currencyTypeId = :currencyTypeId) " +
            "AND (coalesce(:startDate, NULL) IS NULL OR i.creationDate >= :startDate) " +
            "AND (coalesce(:endDate, NULL) IS NULL OR i.creationDate <= :endDate)")
    Page<Object[]> findAllByParamsWithJoin(@Param("userId") UUID userId,
                                           @Param("categoryId") UUID categoryId,
                                           @Param("currencyTypeId") UUID currencyTypeId,
                                           @Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate,
                                           Pageable pageable);
}