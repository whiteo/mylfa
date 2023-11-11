package de.whiteo.mylfa.api;

import de.whiteo.mylfa.domain.IncomeCategory;
import de.whiteo.mylfa.dto.incomecategory.IncomeCategoryCreateOrUpdateRequest;
import de.whiteo.mylfa.dto.incomecategory.IncomeCategoryResponse;
import de.whiteo.mylfa.security.AuthInterceptor;
import de.whiteo.mylfa.service.IncomeCategoryService;
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
@RequestMapping("/api/v1/income-category")
public class IncomeCategoryRestController extends
        AbstractRestController<IncomeCategory, IncomeCategoryResponse, IncomeCategoryCreateOrUpdateRequest> {

    private final AuthInterceptor authInterceptor;
    private final IncomeCategoryService service;

    public IncomeCategoryRestController(IncomeCategoryService service, AuthInterceptor authInterceptor) {
        super(service, authInterceptor);

        this.authInterceptor = authInterceptor;
        this.service = service;
    }

    @GetMapping()
    public ResponseEntity<Page<IncomeCategoryResponse>> findAll(
            @RequestParam(value = "hide", required = false) Boolean hide, Pageable pageable) {
        log.debug("Start call 'findAll' with hide {} and parameters: {}", hide, pageable);
        Page<IncomeCategoryResponse> page = service.findAll(authInterceptor.getUserName(), hide, pageable);
        log.debug("End call 'findAll' with answer {}", page);
        return ResponseEntity.ok(page);
    }
}