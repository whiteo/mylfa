package de.whiteo.mylfa.mapper;

import org.springframework.stereotype.Component;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@Component
public interface AbstractMapper<E, D> {

    D toResponse(E entity);
}