package com.jenu.controller;

import com.jenu.exception.UserException;
import com.jenu.payload.dto.UserDto;
import com.jenu.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserDto> getUserProfile(
            @RequestHeader("X-User-Email") String email
    ) throws Exception {
        UserDto userDto=userService.getUserByEmail(email);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping()
    public ResponseEntity<List<UserDto>> getUsers() throws UserException {

        List<UserDto> users=userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) throws Exception {
        UserDto userDto=userService.getUserById(id);
        return ResponseEntity.ok(userDto);
    }




}
