package de.whiteo.mylfa.service;

import de.whiteo.mylfa.domain.CurrencyType;
import de.whiteo.mylfa.domain.Income;
import de.whiteo.mylfa.domain.IncomeCategory;
import de.whiteo.mylfa.domain.User;
import de.whiteo.mylfa.dto.currencytype.CurrencyTypeResponse;
import de.whiteo.mylfa.dto.income.IncomeCreateOrUpdateRequest;
import de.whiteo.mylfa.dto.income.IncomeFindAllRequest;
import de.whiteo.mylfa.dto.income.IncomeResponse;
import de.whiteo.mylfa.dto.incomecategory.IncomeCategoryResponse;
import de.whiteo.mylfa.exception.NotFoundObjectException;
import de.whiteo.mylfa.mapper.CurrencyTypeMapper;
import de.whiteo.mylfa.mapper.IncomeCategoryMapper;
import de.whiteo.mylfa.mapper.IncomeMapper;
import de.whiteo.mylfa.repository.CurrencyTypeRepository;
import de.whiteo.mylfa.repository.IncomeCategoryRepository;
import de.whiteo.mylfa.repository.IncomeRepository;
import de.whiteo.mylfa.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
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
public class IncomeService extends AbstractService<Income, IncomeResponse, IncomeCreateOrUpdateRequest> {

    private final CurrencyTypeRepository currencyTypeRepository;
    private final IncomeCategoryRepository categoryRepository;
    private final CurrencyTypeMapper currencyTypeMapper;
    private final IncomeCategoryMapper categoryMapper;
    private final IncomeRepository repository;
    private final UserService userService;
    private final IncomeMapper mapper;

    public IncomeService(IncomeCategoryRepository categoryRepository, CurrencyTypeRepository currencyTypeRepository,
                         CurrencyTypeMapper currencyTypeMapper, IncomeCategoryMapper categoryMapper,
                         IncomeRepository repository, UserService userService, IncomeMapper mapper) {
        super(repository, mapper, userService);

        this.categoryRepository = categoryRepository;
        this.currencyTypeRepository = currencyTypeRepository;
        this.currencyTypeMapper = currencyTypeMapper;
        this.categoryMapper = categoryMapper;
        this.userService = userService;
        this.repository = repository;
        this.mapper = mapper;
    }

    public Page<IncomeResponse> findAll(String userName, IncomeFindAllRequest request, Pageable pageable) {
        User user = userService.findByEmail(userName);

        categoryRepository.getOrNull(request.getCategoryId());
        currencyTypeRepository.getOrNull(request.getCurrencyTypeId());

        Page<Object[]> page = repository.findAllByParamsWithJoin(user.getId(),
                request.getCategoryId(),
                request.getCurrencyTypeId(),
                DateUtil.setCorrectTime(request.getStartDate(), true),
                DateUtil.setCorrectTime(request.getEndDate(), false),
                pageable);

        List<IncomeResponse> responses = page.getContent()
                .stream()
                .map(this::mapObjectsToResponse).toList();

        return new PageImpl<>(responses, pageable, page.getTotalElements());
    }

    @Override
    public IncomeResponse findById(String userName, UUID id) {
        User user = userService.findByEmail(userName);

        List<Object[]> result = repository.findByUserIdAndId(user.getId(), id);
        if (result.isEmpty()) {
            throw new NotFoundObjectException("Income with specified ID not found");
        }

        return mapObjectsToResponse(result.get(0));
    }

    public IncomeResponse create(String userName, IncomeCreateOrUpdateRequest request) {
        User user = userService.findByEmail(userName);

        IncomeCategory category = categoryRepository.getOrThrow(request.getCategoryId());
        CurrencyType currencyType = currencyTypeRepository.getOrThrow(request.getCurrencyTypeId());

        Income income = new Income();
        income.setCategoryId(category.getId());
        income.setDescription(request.getDescription());
        income.setCurrencyTypeId(currencyType.getId());
        income.setAmount(request.getAmount());
        income.setUserId(user.getId());
        return mapper.toResponse(
                repository.save(income),
                currencyTypeMapper.toResponse(currencyType),
                categoryMapper.toResponse(category));
    }

    public IncomeResponse update(String userName, UUID id, IncomeCreateOrUpdateRequest request) {
        User user = userService.findByEmail(userName);

        Income income = repository.getByIdAndUserId(id, user.getId());
        IncomeCategory category = categoryRepository.getOrThrow(request.getCategoryId());
        CurrencyType currencyType = currencyTypeRepository.getOrThrow(request.getCurrencyTypeId());

        checkCurrencyTypeIds(income.getCurrencyTypeId(), currencyType.getId());

        income.setCategoryId(category.getId());
        income.setDescription(request.getDescription());
        income.setAmount(request.getAmount());
        return mapper.toResponse(
                repository.save(income),
                currencyTypeMapper.toResponse(currencyType),
                categoryMapper.toResponse(category));
    }

    private IncomeResponse mapObjectsToResponse(Object[] objects) {
        Income income = (Income) objects[0];
        CurrencyType currencyType = (CurrencyType) objects[1];
        IncomeCategory category = (IncomeCategory) objects[2];
        CurrencyTypeResponse currencyTypeResponse = currencyTypeMapper.toResponse(currencyType);
        IncomeCategoryResponse categoryResponse = categoryMapper.toResponse(category);
        return mapper.toResponse(income, currencyTypeResponse, categoryResponse);
    }
}