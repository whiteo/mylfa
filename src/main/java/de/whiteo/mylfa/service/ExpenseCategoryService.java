package de.whiteo.mylfa.service;

import de.whiteo.mylfa.aspect.NoModifyDemoMode;
import de.whiteo.mylfa.domain.ExpenseCategory;
import de.whiteo.mylfa.domain.User;
import de.whiteo.mylfa.dto.expensecategory.ExpenseCategoryCreateOrUpdateRequest;
import de.whiteo.mylfa.dto.expensecategory.ExpenseCategoryFindAllRequest;
import de.whiteo.mylfa.dto.expensecategory.ExpenseCategoryResponse;
import de.whiteo.mylfa.exception.ExecutionConflictException;
import de.whiteo.mylfa.exception.NotFoundObjectException;
import de.whiteo.mylfa.mapper.ExpenseCategoryMapper;
import de.whiteo.mylfa.repository.ExpenseCategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@Slf4j
@Service
public class ExpenseCategoryService extends
        AbstractService<ExpenseCategory, ExpenseCategoryResponse, ExpenseCategoryCreateOrUpdateRequest> {

    private final ExpenseCategoryRepository repository;
    private final ExpenseCategoryMapper mapper;
    private final UserService userService;

    public ExpenseCategoryService(ExpenseCategoryRepository repository, ExpenseCategoryMapper mapper,
                                  UserService userService) {
        super(repository, mapper, userService);

        this.userService = userService;
        this.repository = repository;
        this.mapper = mapper;
    }

    public Page<ExpenseCategoryResponse> findAll(String userName, ExpenseCategoryFindAllRequest request, Pageable pageable) {
        User user = userService.findByEmail(userName);

        Page<Object[]> page = repository.findAllByParamsWithJoin(user.getId(), request.getHide(), pageable);

        List<ExpenseCategoryResponse> responses = page.stream()
                .map(this::mapObjectsToResponse).toList();

        return new PageImpl<>(responses, pageable, page.getTotalElements());
    }

    @Override
    public ExpenseCategoryResponse findById(String userName, UUID id) {
        User user = userService.findByEmail(userName);

        List<Object[]> result = repository.findByUserIdAndId(user.getId(), id);
        if (result.isEmpty()) {
            throw new NotFoundObjectException("Expense category with specified ID not found");
        }

        return mapObjectsToResponse(result.get(0));
    }

    @NoModifyDemoMode
    public ExpenseCategoryResponse create(String userName, ExpenseCategoryCreateOrUpdateRequest request) {
        User user = userService.findByEmail(userName);

        checkUniqueName(request.getName());

        ExpenseCategory parentCategory = repository.getOrNull(request.getParentId());

        ExpenseCategory expenseCategory = new ExpenseCategory();
        expenseCategory.setName(request.getName());
        expenseCategory.setHide(request.isHide());
        expenseCategory.setParentId(request.getParentId());
        expenseCategory.setUserId(user.getId());
        return mapper.toResponse(repository.save(expenseCategory), mapper.toResponse(parentCategory));
    }

    @NoModifyDemoMode
    public ExpenseCategoryResponse update(String userName, UUID id, ExpenseCategoryCreateOrUpdateRequest request) {
        User user = userService.findByEmail(userName);

        checkUniqueName(request.getName());

        ExpenseCategory parentCategory = repository.getOrNull(request.getParentId());

        ExpenseCategory expenseCategory = repository.getByIdAndUserId(id, user.getId());
        expenseCategory.setName(request.getName());
        expenseCategory.setHide(request.isHide());
        expenseCategory.setParentId(request.getParentId());
        return mapper.toResponse(repository.save(expenseCategory), mapper.toResponse(parentCategory));
    }

    private void checkUniqueName(String name) {
        if (StringUtils.isNotBlank(name)) {
            repository.findByNameIgnoreCase(name).ifPresent(
                    existingEntity -> {
                        throw new ExecutionConflictException("Object named '" +
                                name + "' already exists in the system");
                    });
        }
    }

    private ExpenseCategoryResponse mapObjectsToResponse(Object[] objects) {
        ExpenseCategory category = (ExpenseCategory) objects[0];
        ExpenseCategory parentCategory = (ExpenseCategory) objects[1];
        ExpenseCategoryResponse categoryResponse = mapper.toResponse(category);
        if (null != parentCategory) {
            categoryResponse.setParent(mapper.toResponse(parentCategory));
        }
        return categoryResponse;
    }
}