package com.jenu.payload.dto;

import com.jenu.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserDto {

    private Long id;
    private String email;
    private String password;
    private String fullName;
    private String phoneNumber;
    private UserRole userRole;
    private LocalDateTime lastLogin;
}
