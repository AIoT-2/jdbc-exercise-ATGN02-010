package com.nhnacademy.jdbc.student.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import com.nhnacademy.jdbc.student.domain.Student;
import com.nhnacademy.jdbc.student.repository.StudentRepository;
import com.nhnacademy.jdbc.util.DbUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PreparedStatementStudentRepository implements StudentRepository {

    @Override
    public int save(Student student) {
        // todo#1 학생 등록
        try (Connection connection = DbUtils.getConnection();
                PreparedStatement statement = connection
                        .prepareStatement("insert into jdbc_students(id, name, gender, age) values(?, ?, ?, ?)");) {
            statement.setString(1, student.getId());
            statement.setString(2, student.getName());
            statement.setString(3, student.getGender().name());
            statement.setInt(4, student.getAge());

            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Student> findById(String id) {
        // todo#2 학생 조회
        try (Connection connection = DbUtils.getConnection();
                PreparedStatement statement = connection
                        .prepareStatement("select * from jdbc_students where id=?");) {
            statement.setString(1, id);

            try (ResultSet resultSet = statement.executeQuery();) {
                if (resultSet.next()) {
                    Student student = new Student(resultSet.getString("id"), resultSet.getString("name"),
                            Student.GENDER.valueOf(
                                    resultSet.getString("gender")),
                            resultSet.getInt("age"));

                    return Optional.of(student);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    @Override
    public int update(Student student) {
        // todo#3 학생 수정 , name 수정
        try (Connection connection = DbUtils.getConnection();
                PreparedStatement statement = connection
                        .prepareStatement("update jdbc_students set name=?, gender=?, age=? where id=?");) {
            statement.setString(1, student.getName());
            statement.setString(2, student.getGender().name());
            statement.setInt(3, student.getAge());
            statement.setString(4, student.getId());

            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int deleteById(String id) {
        // todo#4 학생 삭제
        try (Connection connection = DbUtils.getConnection();
                PreparedStatement statement = connection
                        .prepareStatement("delete from jdbc_students where id=?");) {
            statement.setString(1, id);

            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
