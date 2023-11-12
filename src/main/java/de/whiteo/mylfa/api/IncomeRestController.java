package de.whiteo.mylfa.api;

import de.whiteo.mylfa.domain.Income;
import de.whiteo.mylfa.dto.income.IncomeCreateOrUpdateRequest;
import de.whiteo.mylfa.dto.income.IncomeFindAllRequest;
import de.whiteo.mylfa.dto.income.IncomeResponse;
import de.whiteo.mylfa.security.AuthInterceptor;
import de.whiteo.mylfa.service.IncomeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

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

    @PostMapping("/find")
    public ResponseEntity<Page<IncomeResponse>> findAll(IncomeFindAllRequest request, Pageable pageable) {
        Page<IncomeResponse> page = service.findAll(authInterceptor.getUserName(), request, pageable);
        return ResponseEntity.ok(page);
    }
}