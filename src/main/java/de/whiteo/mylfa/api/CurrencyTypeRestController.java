package de.whiteo.mylfa.api;

import de.whiteo.mylfa.domain.CurrencyType;
import de.whiteo.mylfa.dto.currencytype.CurrencyTypeCreateOrUpdateRequest;
import de.whiteo.mylfa.dto.currencytype.CurrencyTypeResponse;
import de.whiteo.mylfa.security.AuthInterceptor;
import de.whiteo.mylfa.service.CurrencyTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@Slf4j
@RestController
@RequestMapping("/api/v1/currency-type")
public class CurrencyTypeRestController extends
        AbstractRestController<CurrencyType, CurrencyTypeResponse, CurrencyTypeCreateOrUpdateRequest> {

    private final AuthInterceptor authInterceptor;
    private final CurrencyTypeService service;

    public CurrencyTypeRestController(CurrencyTypeService service, AuthInterceptor authInterceptor) {
        super(service, authInterceptor);

        this.authInterceptor = authInterceptor;
        this.service = service;
    }

    @GetMapping()
    public ResponseEntity<Page<CurrencyTypeResponse>> findAll(
            @RequestParam(value = "hide", required = false) Boolean hide, Pageable pageable) {
        log.debug("Start call 'findAll' with hide {} and parameters: {}", hide, pageable);
        Page<CurrencyTypeResponse> page = service.findAll(authInterceptor.getUserName(), hide, pageable);
        log.debug("End call 'findAll' with answer {}", page);
        return ResponseEntity.ok(page);
    }
}