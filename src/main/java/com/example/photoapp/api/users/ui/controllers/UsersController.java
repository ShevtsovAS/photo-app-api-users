package com.example.photoapp.api.users.ui.controllers;

import com.example.photoapp.api.users.service.UserService;
import com.example.photoapp.api.users.shared.UserDto;
import com.example.photoapp.api.users.ui.model.CreateUserRequest;
import com.example.photoapp.api.users.ui.model.CreateUserResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {

    private final Environment environment;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @GetMapping("/status/check")
    public String status() {
        return "Users service is working on port " + environment.getProperty("local.server.port") + ", with token secret " + environment.getProperty("token.secret");
    }

    @PostMapping
    public ResponseEntity<CreateUserResponse> createUser(@Valid @RequestBody CreateUserRequest userDetails) {
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);
        CreateUserResponse createdUser = modelMapper.map(userService.createUser(userDto), CreateUserResponse.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
}
