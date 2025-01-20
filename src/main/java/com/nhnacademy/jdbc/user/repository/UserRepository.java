package com.nhnacademy.jdbc.user.repository;

import java.util.Optional;

import com.nhnacademy.jdbc.user.domain.User;

public interface UserRepository {

    Optional<User> findByUserIdAndUserPassword(String userId, String userPassword);

    Optional<User> findById(String userId);

    int save(User user);

    int updateUserPasswordByUserId(String userId, String userPassword);

    int deleteByUserId(String userId);

}
