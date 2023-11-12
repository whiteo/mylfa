package de.whiteo.mylfa.service;

import de.whiteo.mylfa.aspect.NoModifyDemoMode;
import de.whiteo.mylfa.domain.IncomeCategory;
import de.whiteo.mylfa.domain.User;
import de.whiteo.mylfa.dto.incomecategory.IncomeCategoryCreateOrUpdateRequest;
import de.whiteo.mylfa.dto.incomecategory.IncomeCategoryFindAllRequest;
import de.whiteo.mylfa.dto.incomecategory.IncomeCategoryResponse;
import de.whiteo.mylfa.exception.ExecutionConflictException;
import de.whiteo.mylfa.exception.NotFoundObjectException;
import de.whiteo.mylfa.mapper.IncomeCategoryMapper;
import de.whiteo.mylfa.repository.IncomeCategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@Slf4j
@Service
public class IncomeCategoryService extends
        AbstractService<IncomeCategory, IncomeCategoryResponse, IncomeCategoryCreateOrUpdateRequest> {

    private final IncomeCategoryRepository repository;
    private final IncomeCategoryMapper mapper;
    private final UserService userService;

    public IncomeCategoryService(IncomeCategoryRepository repository, IncomeCategoryMapper mapper,
                                 UserService userService) {
        super(repository, mapper, userService);

        this.userService = userService;
        this.repository = repository;
        this.mapper = mapper;
    }

    public Page<IncomeCategoryResponse> findAll(String userName, IncomeCategoryFindAllRequest request, Pageable pageable) {
        User user = userService.findByEmail(userName);

        Page<Object[]> page = repository.findAllByParamsWithJoin(user.getId(), request.getHide(), pageable);

        List<IncomeCategoryResponse> responses = page.stream()
                .map(this::mapObjectsToResponse).collect(Collectors.toList());

        return new PageImpl<>(responses, pageable, page.getTotalElements());
    }

    @Override
    public IncomeCategoryResponse findById(String userName, UUID id) {
        User user = userService.findByEmail(userName);

        List<Object[]> result = repository.findByUserIdAndId(user.getId(), id);
        if (result.isEmpty()) {
            throw new NotFoundObjectException("Income category with specified ID not found");
        }

        return mapObjectsToResponse(result.get(0));
    }

    @NoModifyDemoMode
    public IncomeCategoryResponse create(String userName, IncomeCategoryCreateOrUpdateRequest request) {
        User user = userService.findByEmail(userName);

        checkUniqueName(request.getName());

        IncomeCategory parentCategory = repository.getOrNull(request.getParentId());

        IncomeCategory incomeCategory = new IncomeCategory();
        incomeCategory.setName(request.getName());
        incomeCategory.setHide(request.isHide());
        incomeCategory.setUserId(user.getId());
        incomeCategory.setParentId(request.getParentId());
        return mapper.toResponse(repository.save(incomeCategory), mapper.toResponse(parentCategory));
    }

    @NoModifyDemoMode
    public IncomeCategoryResponse update(String userName, UUID id, IncomeCategoryCreateOrUpdateRequest request) {
        User user = userService.findByEmail(userName);

        checkUniqueName(request.getName());

        IncomeCategory parentCategory = repository.getOrNull(request.getParentId());

        IncomeCategory incomeCategory = repository.getByIdAndUserId(id, user.getId());
        incomeCategory.setName(request.getName());
        incomeCategory.setHide(request.isHide());
        incomeCategory.setParentId(request.getParentId());
        return mapper.toResponse(repository.save(incomeCategory), mapper.toResponse(parentCategory));
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

    private IncomeCategoryResponse mapObjectsToResponse(Object[] objects) {
        IncomeCategory category = (IncomeCategory) objects[0];
        IncomeCategory parentCategory = (IncomeCategory) objects[1];
        IncomeCategoryResponse categoryResponse = mapper.toResponse(category);
        if (null != parentCategory) {
            categoryResponse.setParent(mapper.toResponse(parentCategory));
        }
        return categoryResponse;
    }
}