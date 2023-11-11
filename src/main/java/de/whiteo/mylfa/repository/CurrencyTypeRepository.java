package de.whiteo.mylfa.repository;

import de.whiteo.mylfa.domain.CurrencyType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

public interface CurrencyTypeRepository extends AbstractRepository<CurrencyType> {

    Optional<CurrencyType> findByNameIgnoreCase(String name);

    @Query(value = "SELECT c FROM CurrencyType c " +
            "WHERE c.userId = :userId " +
            "AND (:hide IS NULL OR c.hide = :hide )")
    Page<CurrencyType> findAllByUserIdAndHide(@Param("userId") UUID userId, @Param("hide") Boolean hide, Pageable pageable);
}