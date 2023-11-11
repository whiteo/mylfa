package de.whiteo.mylfa.mapper;

import de.whiteo.mylfa.domain.User;
import de.whiteo.mylfa.dto.user.UserLoginResponse;
import de.whiteo.mylfa.dto.user.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@Mapper(componentModel = "spring")
public interface UserMapper extends AbstractMapper<User, UserResponse> {

    @Override
    UserResponse toResponse(User entity);

    @Mapping(target = "token", source = "token")
    @Mapping(target = "user", source = "user")
    UserLoginResponse toLoginResponse(UserResponse user, String token);
}