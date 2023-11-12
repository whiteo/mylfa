package de.whiteo.mylfa.builder;

import de.whiteo.mylfa.domain.CurrencyType;
import de.whiteo.mylfa.dto.currencytype.CurrencyTypeCreateOrUpdateRequest;
import de.whiteo.mylfa.dto.currencytype.CurrencyTypeFindAllRequest;
import de.whiteo.mylfa.repository.CurrencyTypeRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;
import java.util.UUID;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@RequiredArgsConstructor
public class CurrencyTypeBuilder {

    private final static Random random = new Random();
    private final CurrencyTypeRepository repository;

    public static CurrencyTypeCreateOrUpdateRequest buildRequest(String name) {
        CurrencyTypeCreateOrUpdateRequest request = new CurrencyTypeCreateOrUpdateRequest();
        request.setName(name);
        request.setHide(new Random().nextBoolean());
        return request;
    }

    public static CurrencyTypeFindAllRequest buildFindAllRequest() {
        CurrencyTypeFindAllRequest request = new CurrencyTypeFindAllRequest();
        request.setHide(random.nextBoolean());
        return request;
    }

    public static CurrencyType buildCurrencyType() {
        CurrencyType currencyType = new CurrencyType();
        currencyType.setId(UUID.randomUUID());
        currencyType.setHide(random.nextBoolean());
        currencyType.setName(RandomStringUtils.randomAlphabetic(20));
        return currencyType;
    }

    public CurrencyType buildEntity(String name, UUID userId) {
        CurrencyType currencyType = repository.findByNameIgnoreCase(name).orElse(new CurrencyType());
        currencyType.setHide(new Random().nextBoolean());
        currencyType.setName(name);
        currencyType.setUserId(userId);
        return repository.save(currencyType);
    }
}