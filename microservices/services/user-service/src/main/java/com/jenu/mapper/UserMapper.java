package com.jenu.mapper;

import com.jenu.model.User;
import com.jenu.payload.dto.UserDto;
import com.jenu.payload.request.AirportRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;


public class UserMapper {

    public static UserDto ConvertToUserDto(User user) {
        if(user == null) return null;
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .phoneNumber(String.valueOf(user.getPhoneNumber()))
                .userRole(user.getRole())
                .lastLogin(user.getLastLogin())
                .build();
    }
    public static List<UserDto> toUserDtoList(List<User> users) {
        if(users == null) return null;
        return users.stream().map(UserMapper::ConvertToUserDto).collect(Collectors.toList());
    }
}
