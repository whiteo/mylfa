package de.whiteo.mylfa.mapper;

import de.whiteo.mylfa.domain.User;
import de.whiteo.mylfa.dto.user.UserResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

class UserMapperTest {

    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);
    private final Random random = new Random();

    @Test
    void testToResponse() {
        User user = buildEntity();
        UserResponse response = mapper.toResponse(user);
        assertEquals(user.getId(), response.getId());
        assertEquals(user.getEmail(), response.getEmail());
        assertEquals(user.isVerified(), response.isVerified());
    }

    private User buildEntity() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail(RandomStringUtils.randomAlphabetic(20));
        user.setVerified(random.nextBoolean());
        return user;
    }
}