package com.shelter.mykyda.security.api;

import com.shelter.mykyda.dto.UserDTO;
import com.shelter.mykyda.security.dto.UserLoginDto;
import com.shelter.mykyda.security.dto.UserRegistrationDto;
import com.shelter.mykyda.security.exception.NotFoundException;
import com.shelter.mykyda.security.mapper.UserMapper;
import com.shelter.mykyda.security.service.AuthService;
import com.shelter.mykyda.security.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody UserLoginDto userLoginDto) {
        return authService.login(userLoginDto);
    }

    @PostMapping(value = "/registration")
    public ResponseEntity<String> registration(@RequestBody @Valid UserRegistrationDto userRegistrationDto) {
        System.out.println("sda");
        return authService.registration(userRegistrationDto);
    }

    @GetMapping("/currentAccount")
    public ResponseEntity<UserDTO> getCurrentAccount(Principal principal) {
        if (principal == null) {
            throw new NotFoundException("User not found");
        }
        return ResponseEntity.ok(userMapper.userToUserDto(userService.findByUsername(principal.getName())));
    }


}


