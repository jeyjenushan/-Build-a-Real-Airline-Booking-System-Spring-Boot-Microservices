package com.jenu.client;


import com.jenu.exception.UserException;
import com.jenu.payload.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", fallback = UserClientFallback.class)
public interface UserClient {

    @GetMapping("/api/users/{id}")
    UserDto getUserById(
            @PathVariable Long id) throws UserException;
}


