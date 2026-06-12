package com.jenu.service;

import com.jenu.exception.UserException;
import com.jenu.model.User;
import com.jenu.payload.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto getUserByEmail(String email) throws UserException;
    UserDto getUserById(Long id) throws UserException;
    List<UserDto> getAllUsers() throws UserException;
}
