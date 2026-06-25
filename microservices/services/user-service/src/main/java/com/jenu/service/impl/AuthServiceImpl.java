package com.jenu.service.impl;
import com.jenu.configuration.JwtProvider;
import com.jenu.enums.UserRole;
import com.jenu.exception.UserException;
import com.jenu.mapper.UserMapper;
import com.jenu.model.User;
import com.jenu.payload.dto.UserDto;
import com.jenu.payload.response.AuthResponse;
import com.jenu.repository.UserRepository;
import com.jenu.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService customUserDetailsService;


    @Override
    public AuthResponse signup(UserDto userDto) throws UserException {
        User existUser = userRepository.findByEmail(userDto.getEmail());
        if (existUser != null) {
            throw  new UserException("email already Registered");
        }
        if(Objects.equals(userDto.getUserRole(), "ROLE_SYSTEM_ADMIN")){
            throw  new UserException("You cannot register a system admin");
        }


        User newUser=User.builder()
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .phoneNumber(String.valueOf(userDto.getPhoneNumber()))
                .fullName(userDto.getFullName())
                .role(UserRole.valueOf(userDto.getUserRole()))
                .lastLogin(LocalDateTime.now())
                .build();

        User savedUser=userRepository.save(newUser);
        Authentication authentication=
                new UsernamePasswordAuthenticationToken(savedUser.getEmail(), savedUser.getPassword());
        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);
        String jwt= jwtProvider.generateToken(authentication, savedUser.getId());
        AuthResponse authResponse=new AuthResponse();
        authResponse.setJwtToken(jwt);
        authResponse.setUser(UserMapper.ConvertToUserDto(savedUser));
        authResponse.setTitle("Welcome "+savedUser.getFullName());
        authResponse.setMessage("Registered Successfully");

        return authResponse;
    }

    @Override
    public AuthResponse login(String email, String password) throws UserException {
        Authentication authentication=authentication(email,password);
        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);
        User user=userRepository.findByEmail(email);
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
        String jwt=jwtProvider.generateToken(authentication,user.getId());
        AuthResponse authResponse=new AuthResponse();
        authResponse.setJwtToken(jwt);
        authResponse.setMessage("Login Successfully");
        authResponse.setTitle("Welcome Back "+user.getFullName());
        authResponse.setUser(UserMapper.ConvertToUserDto(user));
        return authResponse;
    }

    private Authentication authentication(String email, String password) throws UserException {
        UserDetails userDetails=customUserDetailsService.loadUserByUsername(email);

        if(!passwordEncoder.matches(password,userDetails.getPassword())){
            throw new UserException("Wrong password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }


}
