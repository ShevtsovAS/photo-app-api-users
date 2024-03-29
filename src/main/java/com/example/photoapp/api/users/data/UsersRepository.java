package com.example.photoapp.api.users.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findFirstByEmail(String email);

    Optional<UserEntity> findFirstByUserId(String userId);
}
