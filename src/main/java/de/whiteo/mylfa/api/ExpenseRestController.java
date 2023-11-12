package de.whiteo.mylfa.api;

import de.whiteo.mylfa.domain.Expense;
import de.whiteo.mylfa.dto.expense.ExpenseCreateOrUpdateRequest;
import de.whiteo.mylfa.dto.expense.ExpenseFindAllRequest;
import de.whiteo.mylfa.dto.expense.ExpenseResponse;
import de.whiteo.mylfa.security.AuthInterceptor;
import de.whiteo.mylfa.service.ExpenseService;
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
@RequestMapping("/api/v1/expense")
public class ExpenseRestController extends
        AbstractRestController<Expense, ExpenseResponse, ExpenseCreateOrUpdateRequest> {

    private final AuthInterceptor authInterceptor;
    private final ExpenseService service;

    public ExpenseRestController(ExpenseService service, AuthInterceptor authInterceptor) {
        super(service, authInterceptor);

        this.authInterceptor = authInterceptor;
        this.service = service;
    }

    @PostMapping("/find")
    public ResponseEntity<Page<ExpenseResponse>> findAll(@RequestBody ExpenseFindAllRequest request,
            Pageable pageable) {
        Page<ExpenseResponse> page = service.findAll(authInterceptor.getUserName(), request, pageable);
        return ResponseEntity.ok(page);
    }
}