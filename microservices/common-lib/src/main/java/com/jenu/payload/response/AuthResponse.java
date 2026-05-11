package com.jenu.payload.response;


import com.jenu.payload.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AuthResponse {

    private String jwtToken;
    private String message;
    private String title;
    private UserDto user;
}
