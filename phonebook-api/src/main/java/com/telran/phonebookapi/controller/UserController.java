package com.telran.phonebookapi.controller;

import com.telran.phonebookapi.dto.ContactDto;
import com.telran.phonebookapi.dto.NewPasswordDto;
import com.telran.phonebookapi.dto.RecoveryPasswordDto;
import com.telran.phonebookapi.dto.UserDto;
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
        userService.sendRecoveryToken(recoveryPasswordDto.email);
    }

    @PutMapping("/user/password")
    public void changePassword(@Valid @RequestBody NewPasswordDto newPasswordDto) {
        userService.createNewPassword(newPasswordDto.token, newPasswordDto.password);
    }

    @GetMapping("/bumbum")
    public ResponseEntity<Map<String, String>> getBumbum() {
        Map<String, String> bumbum = new HashMap<>();
        bumbum.put("bum", "bam");
        return new ResponseEntity<>(bumbum, HttpStatus.OK);
    }

    @PutMapping("/user")
    public void editContact(@Valid @RequestBody UserDto userDto) {
        userService.editAllFields(userDto);
    }

    @DeleteMapping("/user/{id}")
    public void removeById(@PathVariable String id) {
        userService.removeById(id);
    }
}

