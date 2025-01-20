package com.nhnacademy.jdbc.user.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import com.nhnacademy.jdbc.user.domain.User;
import com.nhnacademy.jdbc.user.repository.UserRepository;
import com.nhnacademy.jdbc.util.DbUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PreparedStatementUserRepository implements UserRepository {
    @Override
    public Optional<User> findByUserIdAndUserPassword(String userId, String userPassword) {
        // todo#11 -PreparedStatement- 아이디 , 비밀번호가 일치하는 회원조회
        try (Connection connection = DbUtils.getConnection();
                PreparedStatement statement = connection
                        .prepareStatement("select * from jdbc_users where user_id=? and user_password=?")) {
            statement.setString(1, userId);
            statement.setString(2, userPassword);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User(resultSet.getString("user_id"),
                            resultSet.getString("user_name"),
                            resultSet.getString("user_password"));

                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    @Override
    public Optional<User> findById(String userId) {
        // todo#12-PreparedStatement-회원조회
        try (Connection connection = DbUtils.getConnection();
                PreparedStatement statement = connection
                        .prepareStatement("select * from jdbc_users where user_id=?")) {
            statement.setString(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User(resultSet.getString("user_id"),
                            resultSet.getString("user_name"),
                            resultSet.getString("user_password"));

                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    @Override
    public int save(User user) {
        // todo#13-PreparedStatement-회원저장
        try (Connection connection = DbUtils.getConnection();
                PreparedStatement statement = connection
                        .prepareStatement(
                                "insert into jdbc_users(user_id, user_name, user_password) values(?, ?, ?)")) {
            statement.setString(1, user.getUserId());
            statement.setString(2, user.getUserName());
            statement.setString(3, user.getUserPassword());

            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int updateUserPasswordByUserId(String userId, String userPassword) {
        // todo#14-PreparedStatement-회원정보 수정
        try (Connection connection = DbUtils.getConnection();
                PreparedStatement statement = connection
                        .prepareStatement(
                                "update jdbc_users set user_password=? where user_id=?")) {
            statement.setString(1, userPassword);
            statement.setString(2, userId);

            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int deleteByUserId(String userId) {
        // todo#15-PreparedStatement-회원삭제
        try (Connection connection = DbUtils.getConnection();
                PreparedStatement statement = connection
                        .prepareStatement(
                                "delete from jdbc_users where user_id=?")) {
            statement.setString(1, userId);

            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
