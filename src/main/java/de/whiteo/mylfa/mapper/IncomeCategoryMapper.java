package de.whiteo.mylfa.mapper;

import de.whiteo.mylfa.domain.IncomeCategory;
import de.whiteo.mylfa.dto.incomecategory.IncomeCategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@Mapper(componentModel = "spring")
public interface IncomeCategoryMapper extends AbstractMapper<IncomeCategory, IncomeCategoryResponse> {

    @Override
    IncomeCategoryResponse toResponse(IncomeCategory entity);

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "name", source = "entity.name")
    @Mapping(target = "hide", source = "entity.hide")
    @Mapping(target = "parent", source = "parent")
    IncomeCategoryResponse toResponse(IncomeCategory entity, IncomeCategoryResponse parent);
}