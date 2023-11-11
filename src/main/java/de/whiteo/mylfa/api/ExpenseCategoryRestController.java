package de.whiteo.mylfa.api;

import de.whiteo.mylfa.domain.ExpenseCategory;
import de.whiteo.mylfa.dto.expensecategory.ExpenseCategoryCreateOrUpdateRequest;
import de.whiteo.mylfa.dto.expensecategory.ExpenseCategoryResponse;
import de.whiteo.mylfa.security.AuthInterceptor;
import de.whiteo.mylfa.service.ExpenseCategoryService;
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
@RequestMapping("/api/v1/expense-category")
public class ExpenseCategoryRestController extends
        AbstractRestController<ExpenseCategory, ExpenseCategoryResponse, ExpenseCategoryCreateOrUpdateRequest> {

    private final AuthInterceptor authInterceptor;
    private final ExpenseCategoryService service;

    public ExpenseCategoryRestController(ExpenseCategoryService service, AuthInterceptor authInterceptor) {
        super(service, authInterceptor);

        this.authInterceptor = authInterceptor;
        this.service = service;
    }

    @GetMapping()
    public ResponseEntity<Page<ExpenseCategoryResponse>> findAll(
            @RequestParam(value = "hide", required = false) Boolean hide, Pageable pageable) {
        log.debug("Start call 'findAll' with hide {} and parameters: {}", hide, pageable);
        Page<ExpenseCategoryResponse> page = service.findAll(authInterceptor.getUserName(), hide, pageable);
        log.debug("End call 'findAll' with answer {}", page);
        return ResponseEntity.ok(page);
    }
}