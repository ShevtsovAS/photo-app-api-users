package com.example.photoapp.api.users.service;

import com.example.photoapp.api.users.data.AlbumsServiceClient;
import com.example.photoapp.api.users.data.UserEntity;
import com.example.photoapp.api.users.data.UsersRepository;
import com.example.photoapp.api.users.exceptions.UserServiceException;
import com.example.photoapp.api.users.shared.UserDto;
import com.example.photoapp.api.users.ui.model.AlbumResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final ModelMapper modelMapper;
    private final UsersRepository usersRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AlbumsServiceClient albumsServiceClient;

    @Override
    public UserDto createUser(UserDto userDetails) {
        userDetails.setUserId(UUID.randomUUID().toString());
        userDetails.setEncryptedPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));

        UserEntity userEntity = modelMapper.map(userDetails, UserEntity.class);
        return modelMapper.map(usersRepository.save(userEntity), UserDto.class);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = usersRepository.findFirstByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), Collections.emptyList());
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        UserEntity userEntity = usersRepository.findFirstByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
        return modelMapper.map(userEntity, UserDto.class);
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        return usersRepository.findFirstByUserId(userId)
                .map(userEntity -> modelMapper.map(userEntity, UserDto.class))
                .map(this::setAlbums)
                .orElseThrow(() -> new UserServiceException("User not found"));
    }

    private UserDto setAlbums(UserDto userDto) {
        List<AlbumResponse> albums = albumsServiceClient.getAlbums(userDto.getUserId());
        userDto.setAlbums(albums);
        return userDto;
    }
}
