package com.telran.phonebookapi.controller;

import com.telran.phonebookapi.dto.NewPasswordDto;
import com.telran.phonebookapi.dto.RecoveryPasswordDto;
import com.telran.phonebookapi.dto.UserDto;
import com.telran.phonebookapi.dto.UserEmailDto;
import com.telran.phonebookapi.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class UserController {

    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public void addUser(@Valid @RequestBody UserDto userDto) {
        userService.addUser(userDto);
    }

    @GetMapping("/user/activation/{token}")
    public void activateUser(@PathVariable String token) {
        userService.activateUser(token);
    }

    @PostMapping("/user/password/recovery")
    public void recoverPassword(@Valid @RequestBody RecoveryPasswordDto recoveryPasswordDto) {
        userService.sendRecoveryToken(recoveryPasswordDto);
    }

    @PutMapping("/user/password")
    public void changePassword(@Valid @RequestBody NewPasswordDto newPasswordDto) {
        userService.createNewPassword(newPasswordDto);
    }

    @GetMapping("/get-user")
    public UserEmailDto getUserId() {
        return userService.getUserId();
    }
}

