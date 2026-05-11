package com.jenu.service;

import com.jenu.model.User;
import com.jenu.payload.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto getUserByEmail(String email) throws Exception;
    UserDto getUserById(Long id) throws Exception;
    List<UserDto> getAllUsers();
}
