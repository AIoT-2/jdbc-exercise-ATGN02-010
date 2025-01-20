package com.nhnacademy.jdbc.student.repository.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import com.nhnacademy.jdbc.student.domain.Student;
import com.nhnacademy.jdbc.student.repository.StudentRepository;
import com.nhnacademy.jdbc.util.DbUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StatementStudentRepository implements StudentRepository {

    @Override
    public int save(Student student) {
        // todo#1 insert student
        try (Connection connection = DbUtils.getConnection();
                Statement statement = connection.createStatement();) {
            return statement.executeUpdate(
                    String.format("insert into jdbc_students (id, name, gender, age) values ('%s', '%s', '%s', '%s')",
                            student.getId(), student.getName(), student.getGender(), student.getAge()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Student> findById(String id) {
        // todo#2 student 조회
        try (Connection connection = DbUtils.getConnection();
                Statement statement = connection.createStatement();) {
            ResultSet resultSet = statement.executeQuery(
                    String.format("select * from jdbc_students where id='%s'", id));

            if (resultSet.next()) {
                Student student = new Student(resultSet.getString("id"), resultSet.getString("name"),
                        Student.GENDER.valueOf(resultSet.getString("gender")), resultSet.getInt("age"),
                        resultSet.getTimestamp("created_at").toLocalDateTime());
                return Optional.of(student);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    @Override
    public int update(Student student) {
        // todo#3 student 수정, name <- 수정합니다.
        try (Connection connection = DbUtils.getConnection();
                Statement statement = connection.createStatement();) {
            return statement.executeUpdate(
                    String.format("update jdbc_students set name = '%s', gender = '%s', age = %d where id = '%s'",
                            student.getName(), student.getGender(), student.getAge(), student.getId()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int deleteById(String id) {
        // todo#4 student 삭제
        try (Connection connection = DbUtils.getConnection();
                Statement statement = connection.createStatement();) {
            return statement.executeUpdate(
                    String.format("delete from jdbc_students where id = '%s'", id));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
