package com.jenu.service;

import com.jenu.payload.dto.UserDto;
import com.jenu.payload.response.AuthResponse;

public interface AuthService {

    AuthResponse login(String email, String password) throws Exception;
    AuthResponse signup(UserDto userDto) throws Exception;
}
