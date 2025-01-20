package com.nhnacademy.jdbc.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class JdbcExceptionTest {
    static Connection connection;

    @BeforeAll
    static void setUp() {
        connection = DbUtils.getConnection();
    }

    @Test
    @DisplayName("sqlExceptionTest")
    void insert_trhow_sqlException() {

        String sql = "insert into jdbc_students (student_id,student_name,gender,age) values(100,'마르코','M','39')";

        SQLException sqlException = Assertions.assertThrows(SQLException.class, () -> {
            Statement statement = connection.createStatement();
            statement.execute(sql);
        });

        log.info("errorCode:{}", sqlException.getErrorCode());
        log.info("message:{}", sqlException.getMessage());
        log.info("sqlState:{}", sqlException.getSQLState());
    }

    @AfterAll
    static void release() {

        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
