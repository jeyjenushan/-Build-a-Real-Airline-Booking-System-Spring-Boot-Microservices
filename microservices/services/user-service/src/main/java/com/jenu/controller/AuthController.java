package com.jenu.controller;

import com.jenu.exception.UserException;
import com.jenu.payload.dto.UserDto;
import com.jenu.payload.request.LoginRequest;
import com.jenu.payload.response.AuthResponse;
import com.jenu.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@Valid @RequestBody UserDto userDto) throws UserException {
        AuthResponse authResponse = authService.signup(userDto);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> signup(@Valid @RequestBody LoginRequest loginRequest) throws UserException {
        AuthResponse authResponse = authService.login(loginRequest.getEmail(),loginRequest.getPassword());
        return ResponseEntity.ok(authResponse);

    }
}
