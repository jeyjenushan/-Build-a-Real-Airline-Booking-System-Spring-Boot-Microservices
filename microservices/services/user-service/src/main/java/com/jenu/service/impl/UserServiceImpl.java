package com.jenu.service.impl;

import com.jenu.mapper.UserMapper;
import com.jenu.model.User;
import com.jenu.payload.dto.UserDto;
import com.jenu.repository.UserRepository;
import com.jenu.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    @Override
    public UserDto getUserByEmail(String email) throws Exception {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new Exception("User not found with email");
        }
        return UserMapper.ConvertToUserDto(user);
    }

    @Override
    public UserDto getUserById(Long id) throws Exception {
        return UserMapper.ConvertToUserDto(userRepository.findById(id).orElseThrow(
                ()->new Exception("User not found")
        ));
    };

    @Override
    public List<UserDto> getAllUsers() {
        return UserMapper.toUserDtoList(userRepository.findAll());
    }
}
