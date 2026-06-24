package com.jenu.clients;

import com.jenu.payload.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserClientFallback implements UserClient {

    @Override
    public UserDto getUserById(Long userId) {
        log.warn("UserClient fallback triggered for userId={}", userId);
        return null;
    }
}
