package de.whiteo.mylfa.service;

import de.whiteo.mylfa.domain.CurrencyType;
import de.whiteo.mylfa.domain.Expense;
import de.whiteo.mylfa.domain.ExpenseCategory;
import de.whiteo.mylfa.domain.User;
import de.whiteo.mylfa.dto.currencytype.CurrencyTypeResponse;
import de.whiteo.mylfa.dto.expense.ExpenseCreateOrUpdateRequest;
import de.whiteo.mylfa.dto.expense.ExpenseResponse;
import de.whiteo.mylfa.dto.expensecategory.ExpenseCategoryResponse;
import de.whiteo.mylfa.exception.NotFoundObjectException;
import de.whiteo.mylfa.mapper.CurrencyTypeMapper;
import de.whiteo.mylfa.mapper.ExpenseCategoryMapper;
import de.whiteo.mylfa.mapper.ExpenseMapper;
import de.whiteo.mylfa.repository.CurrencyTypeRepository;
import de.whiteo.mylfa.repository.ExpenseCategoryRepository;
import de.whiteo.mylfa.repository.ExpenseRepository;
import de.whiteo.mylfa.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@Slf4j
@Service
public class ExpenseService extends AbstractService<Expense, ExpenseResponse, ExpenseCreateOrUpdateRequest> {

    private final CurrencyTypeRepository currencyTypeRepository;
    private final ExpenseCategoryRepository categoryRepository;
    private final CurrencyTypeMapper currencyTypeMapper;
    private final ExpenseCategoryMapper categoryMapper;
    private final ExpenseRepository repository;
    private final UserService userService;
    private final ExpenseMapper mapper;

    public ExpenseService(ExpenseCategoryRepository categoryRepository, CurrencyTypeRepository currencyTypeRepository,
                          CurrencyTypeMapper currencyTypeMapper, ExpenseCategoryMapper categoryMapper,
                          ExpenseRepository repository, UserService userService, ExpenseMapper mapper) {
        super(repository, mapper, userService);

        this.categoryRepository = categoryRepository;
        this.currencyTypeRepository = currencyTypeRepository;
        this.currencyTypeMapper = currencyTypeMapper;
        this.categoryMapper = categoryMapper;
        this.userService = userService;
        this.repository = repository;
        this.mapper = mapper;
    }

    public Page<ExpenseResponse> findAll(String userName, UUID categoryId, UUID currencyTypeId,
                                         LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        User user = userService.findByEmail(userName);

        Page<Object[]> page = repository.findAllByParamsWithJoin(user.getId(),
                categoryId,
                currencyTypeId,
                DateUtil.setCorrectTime(startDate, true),
                DateUtil.setCorrectTime(endDate, false),
                pageable);

        List<ExpenseResponse> responses = page.getContent()
                .stream()
                .map(this::mapObjectsToResponse).toList();

        return new PageImpl<>(responses, pageable, page.getTotalElements());
    }

    @Override
    public ExpenseResponse findById(String userName, UUID id) {
        User user = userService.findByEmail(userName);

        List<Object[]> result = repository.findByUserIdAndId(user.getId(), id);
        if (result.isEmpty()) {
            throw new NotFoundObjectException("Expense with specified ID not found");
        }

        return mapObjectsToResponse(result.get(0));
    }

    public ExpenseResponse create(String userName, ExpenseCreateOrUpdateRequest request) {
        User user = userService.findByEmail(userName);

        ExpenseCategory category = categoryRepository.getOrThrow(request.getCategoryId());
        CurrencyType currencyType = currencyTypeRepository.getOrThrow(request.getCurrencyTypeId());

        Expense expense = new Expense();
        expense.setCategoryId(category.getId());
        expense.setDescription(request.getDescription());
        expense.setCurrencyTypeId(currencyType.getId());
        expense.setAmount(request.getAmount());
        expense.setUserId(user.getId());
        return mapper.toResponse(
                repository.save(expense),
                currencyTypeMapper.toResponse(currencyType),
                categoryMapper.toResponse(category));
    }

    public ExpenseResponse update(String userName, UUID id, ExpenseCreateOrUpdateRequest request) {
        User user = userService.findByEmail(userName);

        Expense expense = repository.getByIdAndUserId(id, user.getId());
        ExpenseCategory category = categoryRepository.getOrThrow(request.getCategoryId());
        CurrencyType currencyType = currencyTypeRepository.getOrThrow(request.getCurrencyTypeId());

        checkCurrencyTypeIds(expense.getCurrencyTypeId(), currencyType.getId());

        expense.setCategoryId(category.getId());
        expense.setDescription(request.getDescription());
        expense.setAmount(request.getAmount());
        return mapper.toResponse(
                repository.save(expense),
                currencyTypeMapper.toResponse(currencyType),
                categoryMapper.toResponse(category));
    }

    private ExpenseResponse mapObjectsToResponse(Object[] objects) {
        Expense expense = (Expense) objects[0];
        CurrencyType currencyType = (CurrencyType) objects[1];
        ExpenseCategory category = (ExpenseCategory) objects[2];
        CurrencyTypeResponse currencyTypeResponse = currencyTypeMapper.toResponse(currencyType);
        ExpenseCategoryResponse categoryResponse = categoryMapper.toResponse(category);
        return mapper.toResponse(expense, currencyTypeResponse, categoryResponse);
    }
}