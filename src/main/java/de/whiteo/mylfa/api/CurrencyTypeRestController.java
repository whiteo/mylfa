package de.whiteo.mylfa.api;

import de.whiteo.mylfa.domain.CurrencyType;
import de.whiteo.mylfa.dto.currencytype.CurrencyTypeCreateOrUpdateRequest;
import de.whiteo.mylfa.dto.currencytype.CurrencyTypeFindAllRequest;
import de.whiteo.mylfa.dto.currencytype.CurrencyTypeResponse;
import de.whiteo.mylfa.security.AuthInterceptor;
import de.whiteo.mylfa.service.CurrencyTypeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

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

    @PostMapping("/find")
    public ResponseEntity<Page<CurrencyTypeResponse>> findAll(
            @Valid @RequestBody CurrencyTypeFindAllRequest request, Pageable pageable) {
        Page<CurrencyTypeResponse> page = service.findAll(authInterceptor.getUserName(), request, pageable);
        return ResponseEntity.ok(page);
    }
}