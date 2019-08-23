package com.example.photoapp.api.users.service;

import com.example.photoapp.api.users.shared.UserDto;

public interface UserService {
    UserDto createUser(UserDto userDetails);
}
