package com.jenu.client;

import com.jenu.exception.UserException;
import com.jenu.payload.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserClientFallback implements UserClient {

    @Override
    public UserDto getUserById(Long userId) throws UserException {
        log.error("Fallback: Unable to reach user-service for user ID: {}", userId);
        throw new UserException("User service is unavailable. Please try again later.");
    }
}
