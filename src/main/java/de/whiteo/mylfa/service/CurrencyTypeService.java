package de.whiteo.mylfa.service;

import de.whiteo.mylfa.config.NoModifyDemoMode;
import de.whiteo.mylfa.domain.CurrencyType;
import de.whiteo.mylfa.domain.User;
import de.whiteo.mylfa.dto.currencytype.CurrencyTypeCreateOrUpdateRequest;
import de.whiteo.mylfa.dto.currencytype.CurrencyTypeResponse;
import de.whiteo.mylfa.exception.ExecutionConflictException;
import de.whiteo.mylfa.mapper.CurrencyTypeMapper;
import de.whiteo.mylfa.repository.CurrencyTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@Slf4j
@Service
public class CurrencyTypeService extends
        AbstractService<CurrencyType, CurrencyTypeResponse, CurrencyTypeCreateOrUpdateRequest> {

    private final CurrencyTypeRepository repository;
    private final CurrencyTypeMapper mapper;
    private final UserService userService;

    public CurrencyTypeService(CurrencyTypeRepository repository, CurrencyTypeMapper mapper, UserService userService) {
        super(repository, mapper, userService);

        this.userService = userService;
        this.repository = repository;
        this.mapper = mapper;
    }

    public Page<CurrencyTypeResponse> findAll(String userName, Boolean hide, Pageable pageable) {
        User user = userService.findByEmail(userName);

        return repository.findAllByUserIdAndHide(user.getId(), hide, pageable).map(mapper::toResponse);
    }

    @NoModifyDemoMode
    public CurrencyTypeResponse create(String userName, CurrencyTypeCreateOrUpdateRequest request) {
        checkUniqueName(request.getName());

        User user = userService.findByEmail(userName);

        CurrencyType currencyType = new CurrencyType();
        currencyType.setName(request.getName());
        currencyType.setHide(request.isHide());
        currencyType.setUserId(user.getId());
        return mapper.toResponse(repository.save(currencyType));
    }

    @NoModifyDemoMode
    public CurrencyTypeResponse update(String userName, UUID id, CurrencyTypeCreateOrUpdateRequest request) {
        checkUniqueName(request.getName());

        User user = userService.findByEmail(userName);

        CurrencyType currencyType = repository.getByIdAndUserId(id, user.getId());
        currencyType.setName(request.getName());
        currencyType.setHide(request.isHide());
        return mapper.toResponse(repository.save(currencyType));
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
}