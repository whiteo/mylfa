package de.whiteo.mylfa.api;

import de.whiteo.mylfa.domain.IncomeCategory;
import de.whiteo.mylfa.dto.incomecategory.IncomeCategoryCreateOrUpdateRequest;
import de.whiteo.mylfa.dto.incomecategory.IncomeCategoryFindAllRequest;
import de.whiteo.mylfa.dto.incomecategory.IncomeCategoryResponse;
import de.whiteo.mylfa.security.AuthInterceptor;
import de.whiteo.mylfa.service.IncomeCategoryService;
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

    @PostMapping("/find")
    public ResponseEntity<Page<IncomeCategoryResponse>> findAll(
            @Valid @RequestBody IncomeCategoryFindAllRequest request, Pageable pageable) {
        Page<IncomeCategoryResponse> page = service.findAll(authInterceptor.getUserName(), request, pageable);
        return ResponseEntity.ok(page);
    }
}