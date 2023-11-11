package de.whiteo.mylfa.api;

import de.whiteo.mylfa.domain.Income;
import de.whiteo.mylfa.dto.income.IncomeCreateOrUpdateRequest;
import de.whiteo.mylfa.dto.income.IncomeResponse;
import de.whiteo.mylfa.security.AuthInterceptor;
import de.whiteo.mylfa.service.IncomeService;
import de.whiteo.mylfa.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@Slf4j
@RestController
@RequestMapping("/api/v1/income")
public class IncomeRestController extends
        AbstractRestController<Income, IncomeResponse, IncomeCreateOrUpdateRequest> {

    private final AuthInterceptor authInterceptor;
    private final IncomeService service;

    public IncomeRestController(IncomeService service, AuthInterceptor authInterceptor) {
        super(service, authInterceptor);

        this.authInterceptor = authInterceptor;
        this.service = service;
    }

    @GetMapping()
    public ResponseEntity<Page<IncomeResponse>> findAll(
            @RequestParam(value = "categoryId", required = false) UUID categoryId,
            @RequestParam(value = "currencyTypeId", required = false) UUID currencyTypeId,
            @RequestParam(value = "startDate", required = false)
            @DateTimeFormat(pattern = DateUtil.PATTERN_ISO_8601) LocalDateTime startDate,
            @RequestParam(value = "endDate", required = false)
            @DateTimeFormat(pattern = DateUtil.PATTERN_ISO_8601) LocalDateTime endDate,
            Pageable pageable) {
        log.debug("Start call 'findAll' with categoryId {}, currencyTypeId {}, startDate {}, endDate {} and pageable {}",
                categoryId, currencyTypeId, startDate, endDate, pageable);
        Page<IncomeResponse> page = service.findAll(
                authInterceptor.getUserName(), categoryId, currencyTypeId, startDate, endDate, pageable);
        log.debug("End call 'findAll' with answer {}", page);
        return ResponseEntity.ok(page);
    }
}