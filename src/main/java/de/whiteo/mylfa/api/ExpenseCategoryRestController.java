package de.whiteo.mylfa.api;

import de.whiteo.mylfa.domain.ExpenseCategory;
import de.whiteo.mylfa.dto.expensecategory.ExpenseCategoryCreateOrUpdateRequest;
import de.whiteo.mylfa.dto.expensecategory.ExpenseCategoryFindAllRequest;
import de.whiteo.mylfa.dto.expensecategory.ExpenseCategoryResponse;
import de.whiteo.mylfa.security.AuthInterceptor;
import de.whiteo.mylfa.service.ExpenseCategoryService;
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

    @PostMapping("/find")
    public ResponseEntity<Page<ExpenseCategoryResponse>> findAll(
            @Valid @RequestBody ExpenseCategoryFindAllRequest request, Pageable pageable) {
        Page<ExpenseCategoryResponse> page = service.findAll(authInterceptor.getUserName(), request, pageable);
        return ResponseEntity.ok(page);
    }
}